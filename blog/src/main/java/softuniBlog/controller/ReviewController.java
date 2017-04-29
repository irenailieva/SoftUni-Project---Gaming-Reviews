package softuniBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import softuniBlog.bindingModel.ReviewBindingModel;
import softuniBlog.entity.Review;
import softuniBlog.repository.ReviewRepository;
import softuniBlog.repository.UserRepository;
import softuniBlog.entity.User;

@Controller
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/review/create")
    @PreAuthorize("isAuthenticated()")
    public String create(Model model) {
        model.addAttribute("view", "review/create");

        return "base-layout";
    }

    @PostMapping("/review/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(ReviewBindingModel reviewBindingModel) {

        UserDetails user = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());

        Review review = new Review(
                reviewBindingModel.getTitle(),
                reviewBindingModel.getScore(),
                reviewBindingModel.getImageUrl(),
                reviewBindingModel.getContent(),
                userEntity,
                0,
                0,
                reviewBindingModel.getTags().toLowerCase());

        reviewRepository.saveAndFlush(review);

        return "redirect:/";
    }

    @GetMapping("/review/{id}")
    public String details(Model model, @PathVariable int id) {

        if (!this.reviewRepository.exists(id)) {
            return "redirect:/";
        }

        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {

            UserDetails user = (UserDetails) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();

            User userEntity = this.userRepository.findByEmail(user.getUsername());

            model.addAttribute("user", userEntity);
        }

        Review review = this.reviewRepository.findOne(id);

        model.addAttribute("review", review);
        model.addAttribute("view", "review/details");

        return "base-layout";
    }

    @GetMapping("review/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable int id, Model model) {

        if (!this.reviewRepository.exists(id)) {
            return "redirect:/";
        }

        Review review = this.reviewRepository.findOne(id);

        if (!isReviewAuthorOrAdmin(review)) {
            return "redirect:/";
        }

        model.addAttribute("review", review);
        model.addAttribute("view", "review/edit");

        return "base-layout";
    }

    @PostMapping("review/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(@PathVariable int id, ReviewBindingModel reviewBindingModel) {

        if (!this.reviewRepository.exists(id)) {
            return "redirect:/";
        }

        Review review = this.reviewRepository.findOne(id);

        if (!isReviewAuthorOrAdmin(review)) {
            return "redirect:/";
        }

        review.setTitle(reviewBindingModel.getTitle());
        review.setScore(reviewBindingModel.getScore());
        review.setImageUrl(reviewBindingModel.getImageUrl());
        review.setContent(reviewBindingModel.getContent());

        this.reviewRepository.saveAndFlush(review);

        return "redirect:/review/" + review.getId();
    }

    @GetMapping("review/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable int id, Model model) {

        if (!this.reviewRepository.exists(id)) {
            return "redirect:/";
        }

        Review review = this.reviewRepository.findOne(id);

        if (!isReviewAuthorOrAdmin(review)) {
            return "redirect:/";
        }

        model.addAttribute("review", review);
        model.addAttribute("view", "/review/delete");

        return "base-layout";
    }

    @PostMapping("review/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(@PathVariable int id) {

        if (!this.reviewRepository.exists(id)) {
            return "redirect:/";
        }

        Review review = this.reviewRepository.findOne(id);

        if (!isReviewAuthorOrAdmin(review)) {
            return "redirect:/";
        }

        this.reviewRepository.delete(review);

        return "redirect:/";
    }

    @PostMapping("review/{id}/upvote")
    public String upvoteProcess(@PathVariable int id) {

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User user = this.userRepository.findByEmail(principal.getUsername());
        Review review = this.reviewRepository.findOne(id);

        if (user.getDownvotedReviews().contains(review)) {

            user.getDownvotedReviews().remove(review);
            review.setDownvoteCount(review.getDownvoteCount() - 1);
        }

        user.addUpvotedReview(review);
        review.setUpvoteCount(review.getUpvoteCount() + 1);

        this.userRepository.saveAndFlush(user);
        this.reviewRepository.saveAndFlush(review);

        return "redirect:/review/" + review.getId();
    }

    @PostMapping("review/{id}/downvote")
    public String downvoteProcess(@PathVariable int id) {

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User user = this.userRepository.findByEmail(principal.getUsername());
        Review review = this.reviewRepository.findOne(id);

        if (user.getUpvotedReviews().contains(review)) {

            user.getUpvotedReviews().remove(review);
            review.setUpvoteCount(review.getUpvoteCount() - 1);
        }

        user.addDownvotedReview(review);
        review.setDownvoteCount(review.getDownvoteCount() + 1);

        this.userRepository.saveAndFlush(user);
        this.reviewRepository.saveAndFlush(review);

        return "redirect:/review/" + review.getId();
    }

    private boolean isReviewAuthorOrAdmin(Review review) {

        UserDetails user = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());

        return userEntity.isAdmin() || userEntity.isAuthor(review);
    }
}
