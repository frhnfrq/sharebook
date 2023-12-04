import {
  Button,
  Card,
  CardContent,
  Chip,
  Grid,
  Typography,
} from "@mui/material";
import { useAuth } from "../AuthProvider";
import UserHeader from "../components/custom/UserHeader";
import { CardMedia } from "@mui/material";
import axios, { imageBaseURL } from "../network/axios";
import { useEffect, useState } from "react";
import { ApiResponse, Book } from "../types";
import { Link } from "react-router-dom";

export default function HomePage() {
  const authState = useAuth();

  const [books, setBooks] = useState<Book[]>([]);

  const getBooks = async () => {
    try {
      const { data } = await axios.get(`books/suggestion`);
      const response: ApiResponse<Book[]> = data;

      if (response.success) {
        setBooks(response.data || []);
      }
    } catch (error) {
      console.error("Error getting books:", error);
    }
  };

  useEffect(() => {
    getBooks();
  }, []);

  return (
    <>
      <div>
        <UserHeader />

        <div className="container mx-auto mt-8">
          <h1 className="text-3xl">Explore these books</h1>
          <Grid
            container
            spacing={3}
            sx={{ marginTop: "12px", marginBottom: "24px" }}
          >
            {books.map((book) => (
              <Grid item key={book.id} xs={12} sm={6} md={4} lg={3}>
                <Link to={`/books/${book.id}`}>
                  <Card>
                    <CardMedia
                      component="img"
                      alt={book.name}
                      sx={{ height: "200px", objectFit: "cover" }}
                      image={imageBaseURL + book.coverImage}
                      title={book.name}
                    />
                    <CardContent>
                      <Typography variant="h5" component="div" mb={2}>
                        {book.name}
                      </Typography>

                      <Typography variant="h6" mb={2}>
                        {book.price} BDT
                      </Typography>

                      <div className="mb-2">
                        <Typography
                          variant="subtitle2"
                          color="textSecondary"
                          component="div"
                        >
                          Authors:
                        </Typography>
                        {book.authors.map((author, index) => (
                          <Chip key={index} label={author} className="mr-2" />
                        ))}
                      </div>

                      <div className="mb-2">
                        <Typography
                          variant="subtitle2"
                          color="textSecondary"
                          component="div"
                        >
                          Genres:
                        </Typography>
                        {book.genre.map((genre, index) => (
                          <Chip key={index} label={genre} className="mr-2" />
                        ))}
                      </div>

                      <div className="mb-2">
                        <Typography
                          variant="subtitle2"
                          color="textSecondary"
                          component="div"
                        >
                          Owner:
                        </Typography>
                        <Typography variant="body2">
                          {book.user?.name}
                        </Typography>
                      </div>
                    </CardContent>
                  </Card>
                </Link>
              </Grid>
            ))}
          </Grid>
        </div>
      </div>
    </>
  );
}
