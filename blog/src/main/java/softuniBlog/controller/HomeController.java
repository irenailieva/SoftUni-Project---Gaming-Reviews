package softuniBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.expression.Lists;
import softuniBlog.entity.Review;
import softuniBlog.repository.ReviewRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    public String allReviews(Model model) {

        List<Review> allReviews = this.reviewRepository.findAll();
        Collections.reverse(allReviews);

        model.addAttribute("view", "home/all-reviews");
        model.addAttribute("reviews", allReviews);

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
