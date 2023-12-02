import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Button, Card, CardContent, Grid, Typography } from "@mui/material";
import axios from "../network/axios";
import { ApiResponse, Book, Review } from "../types";
import { useAuth } from "../AuthProvider";
import UserHeader from "../components/custom/UserHeader";

interface BookDetailsPageProps {
  // Add any additional props if needed
}

const BookDetailsPage: React.FC<BookDetailsPageProps> = () => {
  const authState = useAuth();

  const { id: bookId } = useParams();

  const [bookDetails, setBookDetails] = useState<Book | null>(null);
  // Assume you have a Review type
  const [reviews, setReviews] = useState<Review[]>([]);

  useEffect(() => {
    const fetchBookDetails = async () => {
      try {
        const { data } = await axios.get(`books/${bookId}`);
        const response: ApiResponse<Book> = data;

        if (response.success) {
          setBookDetails(response.data || null);
        }
      } catch (error) {
        console.error("Error fetching book details:", error);
      }
    };

    // Call the function to fetch book details
    fetchBookDetails();

    // For demo purposes, use placeholder reviews
    const placeholderReviews: Review[] = [
      { id: 1, user: authState.user, review: "Great book!", rating: 5 },
      { id: 2, user: authState.user, review: "Interesting plot.", rating: 5 },
      // Add more placeholder reviews as needed
    ];

    setReviews(placeholderReviews);
  }, [bookId]);

  const handleRequestBook = () => {
    // Implement the logic for sending a book request
    console.log("Book requested!");
  };

  return (
    <div>
      <UserHeader />
      <div className="container mx-auto mt-8">
        <Grid container spacing={3}>
          {/* Left side - Book details */}
          <Grid item xs={12} md={6}>
            {bookDetails && (
              <Card>
                <CardContent>
                  <Typography variant="h4" gutterBottom>
                    {bookDetails.name}
                  </Typography>

                  <Typography variant="subtitle1" color="textSecondary" mb={2}>
                    Authors: {bookDetails.authors.join(", ")}
                  </Typography>

                  <Typography variant="subtitle1" color="textSecondary" mb={2}>
                    Genres: {bookDetails.genre.join(", ")}
                  </Typography>

                  <Typography variant="body1" mb={2}>
                    Description: {/* Add book description here */}
                  </Typography>

                  <Typography variant="subtitle1" color="textSecondary" mb={2}>
                    Availability:{" "}
                    {bookDetails.available ? "Available" : "Not Available"}
                  </Typography>

                  <Typography variant="subtitle1" color="textSecondary" mb={2}>
                    Swappable: {bookDetails.swappable ? "Yes" : "No"}
                  </Typography>

                  {bookDetails.available && (
                    <Button
                      variant="contained"
                      color="primary"
                      onClick={handleRequestBook}
                    >
                      Request
                    </Button>
                  )}
                </CardContent>
              </Card>
            )}
          </Grid>

          {/* Right side - Reviews */}
          <Grid
            item
            xs={12}
            md={6}
            style={{ overflowY: "auto", maxHeight: "70vh" }}
          >
            <Typography variant="h5" gutterBottom>
              Reviews
            </Typography>
            {reviews.map((review) => (
              <Card key={review.id} sx={{ marginBottom: "10px" }}>
                <CardContent>
                  <Typography
                    variant="subtitle1"
                    color="textSecondary"
                    gutterBottom
                  >
                    User: {review.user.name}
                  </Typography>
                  <Typography variant="body2">{review.review}</Typography>
                </CardContent>
              </Card>
            ))}
            {/* Add a placeholder for reviews */}
            {reviews.length === 0 && (
              <Typography variant="body2" color="textSecondary">
                No reviews yet.
              </Typography>
            )}
          </Grid>
        </Grid>
      </div>
    </div>
  );
};

export default BookDetailsPage;
