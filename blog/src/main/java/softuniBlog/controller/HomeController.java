package softuniBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softuniBlog.entity.Review;
import softuniBlog.repository.ReviewRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/")
    public String index(Model model) {

        List<Review> reviews = this.reviewRepository.findAll();
        Collections.reverse(reviews);

        if (reviews.size() > 4) {
            reviews = reviews.subList(0, 4);
        }

        model.addAttribute("view", "home/index");
        model.addAttribute("reviews", reviews);

        return "base-layout";
    }

    @GetMapping("/all-reviews")
    public String allReviews(@RequestParam(required = false) Integer page, Model model) {

        List<Review> allReviews = this.reviewRepository.findAll();
        Collections.reverse(allReviews);

        model.addAttribute("view", "home/all-reviews");
        model.addAttribute("reviews", allReviews);

        return "base-layout";
    }

    @GetMapping("/all-reviews/")
    public String allReviewsSearch(@RequestParam String search, Model model) {

        List<Review> allReviews = this.reviewRepository.findAll();
        Collections.reverse(allReviews);

        if (search.equals("")) {
            return "redirect:/all-reviews";
        }

        allReviews = allReviews
                .stream()
                .filter(review -> review.getTags().contains(search.trim().toLowerCase()))
                .collect(Collectors.toList());

        model.addAttribute("view", "home/all-reviews");
        model.addAttribute("reviews", allReviews);
        model.addAttribute("search", search);

        return "base-layout";
    }

    @GetMapping("/all-reviews-by-upvotes")
    public String allReviewsByUpvotes(Model model) {

        List<Review> allReviewsByUpvotes = this.reviewRepository.findAll();

        allReviewsByUpvotes.sort(Comparator.comparing(Review::getUpvoteCount));

        Collections.reverse(allReviewsByUpvotes);

        model.addAttribute("view", "home/all-reviews");
        model.addAttribute("reviews", allReviewsByUpvotes);

        return "base-layout";
    }
}
