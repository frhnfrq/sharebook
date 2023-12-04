import UserHeader from "../components/custom/UserHeader";
import { ChangeEvent, useEffect, useState } from "react";
import {
  Card,
  CardContent,
  CardMedia,
  Chip,
  Grid,
  TextField,
  Typography,
} from "@mui/material";
import { ApiResponse, Book } from "../types";
import axios from "../network/axios";
import { imageBaseURL } from "../network/axios";
import { IconButton } from "@mui/joy";
import { SearchRounded } from "@mui/icons-material";
import { Link, useNavigate, useLocation } from "react-router-dom";

export default function SearchPage() {
  const [searchQuery, setSearchQuery] = useState<string>("");
  const [searchResults, setSearchResults] = useState<Book[]>([]);

  const navigate = useNavigate();
  const location = useLocation();

  const handleSearchChange = (event: ChangeEvent<HTMLInputElement>) => {
    setSearchQuery(event.target.value);
  };

  const handleSearch = async () => {
    try {
      const { data } = await axios.get(`books/search?query=${searchQuery}`);
      const response: ApiResponse<Book[]> = data;

      if (response.success) {
        setSearchResults(response.data || []);
      }
    } catch (error) {
      console.error("Error searching books:", error);
    }
  };

  useEffect(() => {
    navigate(`/search?query=${searchQuery}`);
    if (searchQuery.trim() !== "") {
      handleSearch();
    } else {
      setSearchResults([]);
    }
  }, [searchQuery]);

  useEffect(() => {
    const urlParams = new URLSearchParams(location.search);
    const queryFromUrl = urlParams.get("query");

    if (queryFromUrl) {
      setSearchQuery(queryFromUrl);
    }
  }, [location.search, navigate]);

  return (
    <div>
      <UserHeader />
      <div className="container mx-auto mt-8">
        <div className="flex">
          <TextField
            fullWidth
            label="Search Books"
            variant="outlined"
            value={searchQuery}
            onChange={handleSearchChange}
          />
          <IconButton
            onClick={() => {
              if (searchQuery.length > 0) {
                handleSearch();
              }
            }}
            sx={{ marginLeft: "8px" }}
          >
            <SearchRounded />
          </IconButton>
        </div>

        <Grid
          container
          spacing={3}
          sx={{ marginTop: "12px", marginBottom: "24px" }}
        >
          {searchResults.map((book) => (
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

                    <div className="mb-2">
                      <Typography
                        variant="subtitle2"
                        color="textSecondary"
                        component="div"
                      >
                        Owner:
                      </Typography>
                      <Typography variant="body2">{book.user?.name}</Typography>
                    </div>
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
