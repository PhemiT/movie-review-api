package dev.kunmi.movieapi;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    public Review createReview(String reviewBody, String imdbId) {
        Review review = reviewRepository.insert(new Review(reviewBody));
        mongoTemplate.update(Movie.class)
                .matching(Criteria.where("imdbId").is(imdbId))
                .apply(new Update().push("reviewIds").value(review))
                .first();

        return review;
    }

    public Review updateReview(ObjectId reviewId, String updatedBody) {
        Query query = new Query(Criteria.where("id").is(reviewId));
        Update update = new Update().set("body", updatedBody);
        mongoTemplate.updateFirst(query, update, Review.class);

        return reviewRepository.findById(reviewId).orElse(null);
    }

    public void deleteReview(ObjectId reviewId) {
        Query query = new Query(Criteria.where("id").is(reviewId));
        mongoTemplate.remove(query, Review.class);

        mongoTemplate.update(Movie.class)
                .matching(Criteria.where("reviewIds").in(reviewId))
                .apply(new Update().pull("reviewIds", reviewId))
                .first();
    }


}
