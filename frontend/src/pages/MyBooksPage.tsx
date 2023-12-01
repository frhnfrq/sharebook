import UserHeader from "../components/custom/UserHeader";
import React, { ChangeEvent, useEffect, useState } from "react";
import {
  Button,
  Card,
  CardContent,
  CardMedia,
  Chip,
  FormControlLabel,
  FormGroup,
  Grid,
  Switch,
  Typography,
} from "@mui/material";

import { ApiResponse, Book } from "../types"; // Import the Book type
import axios from "../network/axios";

export default function MyBooksPage() {
  const [books, setBooks] = useState<Book[]>([]);

  const getBooks = async () => {
    try {
      const { data } = await axios.get("books");

      const response: ApiResponse<Book[]> = data;

      if (response.success) {
        setBooks(response.data!.sort((a: Book, b: Book) => a.id - b.id));
      }
    } catch (error) {
      console.log(error);
    } finally {
      // setLoading(false);
    }
  };

  const updateBook = async (book: Book) => {
    try {
      const { data } = await axios.put("books", JSON.stringify(book));

      const response: ApiResponse<Book> = data;

      if (response.success) {
        const index = books.findIndex((b: Book) => {
          return b.id === book.id;
        });
        const newBooks = [...books];
        if (index !== -1) {
          newBooks[index] = book;
          setBooks(newBooks);
        }
      }
    } catch (error) {
      console.log(error);
    } finally {
      // setLoading(false);
    }
  };

  useEffect(() => {
    getBooks();
  }, []);

  return (
    <div>
      <UserHeader />
      <div className="container mx-auto mt-8">
        <Button variant="contained" color="primary">
          Add a Book
        </Button>

        <Grid
          container
          spacing={3}
          sx={{ marginTop: "12px", marginBottom: "24px" }}
        >
          {books.map((book) => (
            <Grid item key={book.id} xs={12} sm={6} md={4} lg={3}>
              <Card>
                <CardMedia
                  component="img"
                  alt={book.name}
                  height="200"
                  image={"https://placekitten.com/400/300"} // replace with actual image
                  title={book.name}
                />
                <CardContent>
                  <Typography variant="h6" component="div" mb={2}>
                    {book.name}
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

                  <FormGroup>
                    <FormControlLabel
                      control={
                        <Switch
                          checked={book.available}
                          onChange={(
                            event: ChangeEvent<HTMLInputElement>,
                            checked: boolean
                          ) => {
                            updateBook({
                              ...book,
                              available: checked,
                            });
                          }}
                        />
                      }
                      label="Available"
                    />
                    <FormControlLabel
                      control={
                        <Switch
                          checked={book.swappable}
                          onChange={(
                            event: ChangeEvent<HTMLInputElement>,
                            checked: boolean
                          ) => {
                            updateBook({
                              ...book,
                              swappable: checked,
                            });
                          }}
                        />
                      }
                      label="Swappable"
                    />
                  </FormGroup>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </div>
    </div>
  );
}
