package softuniBlog.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    private Integer id;

    private String email;

    private String fullName;

    private String password;

    private Set<Role> roles;

    private Set<Review> reviews;

    private Set<Review> upvotedReviews;

    public User(String email, String fullName, String password) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;

        this.roles = new HashSet<>();
        this.reviews = new HashSet<>();
        this.upvotedReviews = new HashSet<>();
    }

    public User() {

        this.roles = new HashSet<>();
        this.reviews = new HashSet<>();
        this.upvotedReviews = new HashSet<>();
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "email", unique = true, nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "fullName", nullable = false)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(name = "password", length = 60, nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles")
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    @OneToMany(mappedBy = "author")
    public Set<Review> getReviews() { return this.reviews; }

    public void setReviews(Set<Review> reviews) { this.reviews = reviews; }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_upvotedReviews")
    public Set<Review> getUpvotedReviews() { return upvotedReviews; }

    public void setUpvotedReviews(Set<Review> upvotedReviews) { this.upvotedReviews = upvotedReviews; }

    public void addUpvotedReview(Review upvotedReview) { this.upvotedReviews.add(upvotedReview); }

    @Transient
    public boolean isAdmin(){

        return this.getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }

    @Transient
    public boolean isAuthor(Review review) {

        return Objects.equals(this.getId(), review.getAuthor().getId());
    }

    @Transient
    public int getReviewCount() {
        return this.reviews.size();
    }

    @Transient
    public boolean hasUpvoted(Review review) {

        return this.getUpvotedReviews()
                .stream()
                .anyMatch(targetReview -> targetReview.getId() == review.getId());
    }
}