import React, { ChangeEvent, useEffect, useState } from "react";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControl,
  InputLabel,
  Input,
  TextField,
  Switch,
  FormControlLabel,
  FormLabel,
  Paper,
  IconButton,
} from "@mui/material";
import { toast } from "react-hot-toast";

import { CloseRounded } from "@mui/icons-material";

import { ApiResponse, Book } from "../types"; // Import the Book type
import axios from "../network/axios";
import { imageBaseURL } from "../network/axios";

interface BookFormProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (book: Book) => void;
  initialBook?: Book;
}

const BookForm: React.FC<BookFormProps> = ({
  isOpen,
  onClose,
  onSubmit,
  initialBook,
}) => {
  const [book, setBook] = useState<Book>(
    initialBook || {
      id: 0,
      name: "",
      authors: [],
      genre: [],
      coverImage: "",
      sampleImages: [],
      available: false,
      swappable: false,
      price: 0,
    }
  );

  useEffect(() => {
    setBook(
      initialBook || {
        id: 0,
        name: "",
        authors: [],
        genre: [],
        coverImage: "",
        sampleImages: [],
        available: false,
        swappable: false,
        price: 0,
      }
    );
  }, [initialBook]);

  const handleInputChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setBook((prevBook) => ({ ...prevBook, [name]: value }));
  };

  const handleSwitchChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, checked } = event.target;
    setBook((prevBook) => ({ ...prevBook, [name]: checked }));
  };

  const handleDynamicFieldChange = (
    event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    field: string
  ) => {
    const { value } = event.target;
    setBook((prevBook) => ({ ...prevBook, [field]: value.split(",") }));
  };

  const handleFileChange = async (
    event: React.ChangeEvent<HTMLInputElement>,
    field: string
  ) => {
    const file = event.target.files?.[0];
    if (file) {
      try {
        const formData = new FormData();
        formData.append("image", file);

        const { data } = await axios.post("image", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });

        console.log(data);

        if (!data.success) {
          toast.error(data.message);
          return;
        }

        if (field === "sampleImages") {
          setBook((prevBook) => ({ ...prevBook, [field]: [data.data] }));
        } else {
          setBook((prevBook) => ({ ...prevBook, [field]: data.data }));
        }
      } catch (error) {
        console.error("Error uploading image:", error);
        // Handle error, e.g., show a message to the user
      }
    }
  };

  const handleRemoveCoverImage = () => {
    setBook((prevBook) => ({ ...prevBook, coverImage: "" }));
  };

  const handleRemoveSampleImage = () => {
    setBook((prevBook) => ({ ...prevBook, sampleImages: [] }));
  };

  const handleSubmit = async () => {
    // Perform validation if needed before submitting

    if (
      book.name.length > 0 &&
      book.authors[0].length > 0 &&
      book.genre[0].length > 0 &&
      book.coverImage.length > 0 &&
      book.sampleImages.length > 0
    ) {
      const { data } = await axios.post("books", JSON.stringify(book));
      const response: ApiResponse<Book> = data;
      if (!response.success) {
        toast.error(response.message ?? "Something went wrong");
      } else {
        if (initialBook) {
          toast.success("Successfully edited the book");
        } else {
          toast.success("Successfully created a book entry");
        }

        onSubmit(response.data!);
        onClose();
      }
    } else {
      toast.error("Please fill all fields");
    }
  };

  return (
    <Dialog open={isOpen} onClose={onClose} fullWidth maxWidth="md">
      <DialogTitle>{initialBook ? "Edit Book" : "Add a Book"}</DialogTitle>
      <DialogContent>
        <FormControl fullWidth margin="normal">
          <InputLabel htmlFor="name">Book Name</InputLabel>
          <Input
            id="name"
            name="name"
            value={book.name}
            onChange={handleInputChange}
          />
        </FormControl>

        <FormControl fullWidth margin="normal">
          <InputLabel htmlFor="Price">Book Price</InputLabel>
          <Input
            id="price"
            name="price"
            value={book.price}
            onChange={handleInputChange}
            type="number"
          />
        </FormControl>

        {/* Dynamic Author Field */}
        <TextField
          fullWidth
          margin="normal"
          label="Authors (comma-separated)"
          value={book.authors.join(",")}
          onChange={(e) => handleDynamicFieldChange(e, "authors")}
        />

        {/* Dynamic Genre Field */}
        <TextField
          fullWidth
          margin="normal"
          label="Genres (comma-separated)"
          value={book.genre.join(",")}
          onChange={(e) => handleDynamicFieldChange(e, "genre")}
        />

        {/* Cover Image File Upload */}

        <div className="mt-4">
          <FormLabel>Cover Image</FormLabel>
        </div>

        {book.coverImage.length > 0 && (
          <Paper
            elevation={3}
            style={{ position: "relative", maxWidth: 200, margin: "10px" }}
          >
            <img
              src={imageBaseURL + book.coverImage}
              alt="Cover Preview"
              style={{ maxWidth: "100%", height: "auto" }}
            />
            <IconButton
              size="small"
              color="error"
              onClick={handleRemoveCoverImage}
              style={{
                position: "absolute",
                top: 0,
                right: 0,
              }}
            >
              <CloseRounded />
            </IconButton>
          </Paper>
        )}

        <Input
          fullWidth
          type="file"
          id="coverImage"
          name="coverImage"
          onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
            handleFileChange(e, "coverImage")
          }
        />

        {/* Sample Image File Upload */}

        <div className="mt-4">
          <FormLabel>Sample Image</FormLabel>
        </div>

        {book.sampleImages.length > 0 && (
          <Paper
            elevation={3}
            style={{ position: "relative", maxWidth: 200, margin: "10px" }}
          >
            <img
              src={imageBaseURL + book.sampleImages[0]}
              alt="Cover Preview"
              style={{ maxWidth: "100%", height: "auto" }}
            />
            <IconButton
              size="small"
              color="error"
              onClick={handleRemoveSampleImage}
              style={{
                position: "absolute",
                top: 0,
                right: 0,
              }}
            >
              <CloseRounded />
            </IconButton>
          </Paper>
        )}

        <Input
          fullWidth
          type="file"
          id="sampleImage"
          name="sampleImage"
          onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
            handleFileChange(e, "sampleImages")
          }
        />

        {initialBook && (
          <FormControlLabel
            control={
              <Switch
                checked={book.available}
                onChange={handleSwitchChange}
                name="available"
              />
            }
            label="Available"
          />
        )}

        {initialBook && (
          <FormControlLabel
            control={
              <Switch
                checked={book.swappable}
                onChange={handleSwitchChange}
                name="swappable"
              />
            }
            label="Swappable"
          />
        )}
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} color="primary">
          Cancel
        </Button>
        <Button onClick={handleSubmit} color="primary">
          {initialBook ? "Save Changes" : "Add Book"}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default BookForm;
