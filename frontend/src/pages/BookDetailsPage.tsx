import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import {
  Button,
  Card,
  CardContent,
  CardMedia,
  Grid,
  Typography,
} from "@mui/material";
import axios, { imageBaseURL } from "../network/axios";
import { ApiResponse, Book, BookRequest, Review } from "../types";
import { useAuth } from "../AuthProvider";
import UserHeader from "../components/custom/UserHeader";
import toast from "react-hot-toast";

interface BookDetailsPageProps {
  // Add any additional props if needed
}

const BookDetailsPage: React.FC<BookDetailsPageProps> = () => {
  const authState = useAuth();

  const { id: bookId } = useParams();

  const [bookDetails, setBookDetails] = useState<Book | null>(null);
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
      { id: 2, user: authState.user, review: "Interesting plot.", rating: 5 },
      { id: 2, user: authState.user, review: "Interesting plot.", rating: 5 },
      { id: 2, user: authState.user, review: "Interesting plot.", rating: 5 },
      { id: 2, user: authState.user, review: "Interesting plot.", rating: 5 },
      { id: 2, user: authState.user, review: "Interesting plot.", rating: 5 },
      { id: 2, user: authState.user, review: "Interesting plot.", rating: 5 },
      { id: 2, user: authState.user, review: "Interesting plot.", rating: 5 },
      { id: 2, user: authState.user, review: "Interesting plot.", rating: 5 },
      { id: 2, user: authState.user, review: "Interesting plot.", rating: 5 },
      { id: 2, user: authState.user, review: "Interesting plot.", rating: 5 },
      { id: 2, user: authState.user, review: "Interesting plot.", rating: 5 },
      { id: 2, user: authState.user, review: "Interesting plot.", rating: 5 },
      { id: 2, user: authState.user, review: "Interesting plot.", rating: 5 },
      { id: 2, user: authState.user, review: "Interesting plot.", rating: 5 },
      { id: 2, user: authState.user, review: "Interesting plot.", rating: 5 },
      // Add more placeholder reviews as needed
    ];

    setReviews(placeholderReviews);
  }, [bookId]);

  const [bookRequests, setBookRequests] = useState<BookRequest[]>([]);

  useEffect(() => {
    const fetchBookRequests = async () => {
      try {
        const { data } = await axios.get(`books/request/${bookId}`);
        const response: ApiResponse<BookRequest[]> = data;

        console.log(data);

        if (response.success) {
          setBookRequests(response.data?.reverse() || []);
        }
      } catch (error) {
        console.error("Error fetching book requests:", error);
      }
    };

    // Call the function to fetch book requests
    fetchBookRequests();
  }, [bookId]);

  const handleRequestBook = async () => {
    const { data } = await axios.post("books/request", {
      bookId: bookId,
    });

    if (data.success) {
      toast.success("You have requested for this book");
    } else {
      toast.error(data.message);
    }
  };

  const handleApproveRequest = async (requestId: number) => {
    try {
      const { data } = await axios.post(`books/request/approve/${requestId}`);

      if (data.success) {
        // Update the bookRequests array to mark the request as approved
        setBookRequests((prevRequests) =>
          prevRequests.map((request) =>
            request.id === requestId ? { ...request, approved: true } : request
          )
        );

        toast.success("Request approved successfully");
      } else {
        toast.error(data.message);
      }
    } catch (error) {
      console.error("Error approving book request:", error);
    }
  };

  const handleRejectRequest = async (requestId: number) => {
    try {
      const { data } = await axios.post(`books/request/reject/${requestId}`);

      if (data.success) {
        // Update the bookRequests array to mark the request as approved
        setBookRequests((prevRequests) =>
          prevRequests.map((request) =>
            request.id === requestId ? { ...request, rejected: true } : request
          )
        );

        toast.success("Request rejected successfully");
      } else {
        toast.error(data.message);
      }
    } catch (error) {
      console.error("Error rejecting book request:", error);
    }
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
                <CardMedia
                  component="img"
                  alt={bookDetails.name}
                  sx={{ height: "200px", objectFit: "cover" }}
                  image={imageBaseURL + bookDetails.coverImage}
                  title={bookDetails.name}
                />
                <CardContent>
                  <Typography variant="h4" gutterBottom>
                    {bookDetails.name}
                  </Typography>

                  <Typography variant="h5" mb={2}>
                    {bookDetails.price} BDT
                  </Typography>

                  <Typography variant="subtitle1" color="textSecondary" mb={2}>
                    Authors: {bookDetails.authors.join(", ")}
                  </Typography>

                  <Typography variant="subtitle1" color="textSecondary" mb={2}>
                    Genres: {bookDetails.genre.join(", ")}
                  </Typography>

                  <Typography variant="subtitle1" color="textSecondary" mb={2}>
                    Availability:{" "}
                    {bookDetails.available ? "Available" : "Not Available"}
                  </Typography>

                  <Typography variant="subtitle1" color="textSecondary" mb={2}>
                    Swappable: {bookDetails.swappable ? "Yes" : "No"}
                  </Typography>

                  {bookDetails.user?.id !== authState.user.id &&
                    bookDetails.available && (
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

            <Typography sx={{ marginTop: "12px" }} variant="h5" gutterBottom>
              Reviews
            </Typography>

            <div style={{ overflowY: "auto", maxHeight: "40vh" }}>
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
            </div>
          </Grid>

          {/* Right side - Reviews */}
          <Grid
            item
            xs={12}
            md={6}
            style={{ overflowY: "auto", maxHeight: "70vh" }}
          >
            {bookDetails && bookDetails.user?.id === authState.user.id && (
              <div>
                <Typography variant="h6" gutterBottom>
                  Book Exchange Requests
                </Typography>
                {bookRequests.map((request) => (
                  <Card key={request.id} sx={{ marginBottom: "10px" }}>
                    <CardContent>
                      <Typography variant="subtitle1" gutterBottom>
                        Requester: {request.user.name}
                      </Typography>
                      <Typography variant="subtitle2" color="textSecondary">
                        Request Time: {request.createdAt}
                      </Typography>
                      <div style={{ marginTop: "10px" }}>
                        <Button
                          variant="contained"
                          color="primary"
                          onClick={() => handleApproveRequest(request.id)}
                          disabled={request.approved || request.rejected}
                          style={{ marginRight: "10px" }}
                        >
                          Approve
                        </Button>
                        <Button
                          variant="contained"
                          color="secondary"
                          onClick={() => handleRejectRequest(request.id)}
                          disabled={request.approved || request.rejected}
                        >
                          Reject
                        </Button>
                      </div>
                    </CardContent>
                  </Card>
                ))}
                {bookRequests.length === 0 && (
                  <Typography variant="body2" color="textSecondary">
                    No book exchange requests yet.
                  </Typography>
                )}
              </div>
            )}
          </Grid>
        </Grid>
      </div>
    </div>
  );
};

export default BookDetailsPage;
