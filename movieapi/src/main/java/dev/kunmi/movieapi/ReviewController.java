package dev.kunmi.movieapi;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Map<String, String> payload) {
        return new ResponseEntity<Review>(reviewService.createReview(payload.get("reviewBody"), payload.get("imdbId")), HttpStatus.CREATED);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(@RequestBody Map<String, String> payload) {
        String reviewId = payload.get("reviewId");
        String updatedBody = payload.get("updatedBody");

        if (reviewId == null || updatedBody == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ObjectId objectId = new ObjectId(reviewId);
        Review updatedReview = reviewService.updateReview(objectId, updatedBody);

        if (updatedReview != null) {
            return new ResponseEntity<>(updatedReview, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable String reviewId) {
        ObjectId objectId = new ObjectId(reviewId);
        reviewService.deleteReview(objectId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
