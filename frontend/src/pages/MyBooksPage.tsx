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
import BookForm from "../components/BookForm";
import { imageBaseURL } from "../network/axios";
import { Link } from "react-router-dom";

export default function MyBooksPage() {
  const [isDialogOpen, setDialogOpen] = useState(false);

  const [books, setBooks] = useState<Book[]>([]);

  const [selectedBook, setSelectedBook] = useState<Book | undefined>();

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
        <Button
          variant="contained"
          color="primary"
          onClick={() => setDialogOpen(true)}
        >
          Add a Book
        </Button>

        <BookForm
          isOpen={selectedBook !== undefined || isDialogOpen}
          onClose={() => {
            setDialogOpen(false);
            setSelectedBook(undefined);
          }}
          onSubmit={(newBook) => {
            setDialogOpen(false);
            getBooks();
            setSelectedBook(undefined);
          }}
          initialBook={selectedBook}
        />

        <Grid
          container
          spacing={3}
          sx={{ marginTop: "12px", marginBottom: "24px" }}
        >
          {books.map((book) => (
            <Grid item key={book.id} xs={12} sm={6} md={4} lg={3}>
              <Link to={"/books/" + book.id}>
                <Card>
                  <CardMedia
                    component="img"
                    alt={book.name}
                    sx={{ height: "200px", objectFit: "cover" }}
                    image={imageBaseURL + book.coverImage}
                    title={book.name}
                  />
                  <CardContent>
                    <Typography variant="h6" component="div" mb={2}>
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
                    <Button
                      variant="outlined"
                      color="primary"
                      onClick={(e) => {
                        e.preventDefault();
                        e.stopPropagation();
                        setSelectedBook(book);
                      }}
                      sx={{ mt: 2 }}
                    >
                      Edit
                    </Button>
                  </CardContent>
                </Card>
              </Link>
            </Grid>
          ))}
        </Grid>
      </div>
    </div>
  );
}
