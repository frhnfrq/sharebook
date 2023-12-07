import { ChangeEvent, useEffect, useState } from "react";
import UserHeader from "../components/custom/UserHeader";
import { ApiResponse, User, Wish } from "../types";
import { useAuth } from "../AuthProvider";
import axios, { imageBaseURL } from "../network/axios";
import toast from "react-hot-toast";
import {
  Button,
  Card,
  CardActions,
  CardContent,
  Chip,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControl,
  FormLabel,
  IconButton,
  Input,
  InputLabel,
  Paper,
  TextField,
  Typography,
} from "@mui/material";
import { CloseRounded } from "@mui/icons-material";

export default function WishlistPage() {
  const [wishlist, setWishlist] = useState<Wish[]>([]);
  const [isAddDialogOpen, setAddDialogOpen] = useState(false);
  const [isEditDialogOpen, setEditDialogOpen] = useState(false);
  const [selectedWish, setSelectedWish] = useState<Wish | null>(null);
  const [newWishName, setNewWishName] = useState("");
  const [newWishAuthors, setNewWishAuthors] = useState("");

  useEffect(() => {
    // Fetch wishlist when the component mounts
    fetchWishlist();
  }, []);

  const fetchWishlist = async () => {
    try {
      const { data } = await axios.get(`wishlist`);
      const response: ApiResponse<Wish[]> = data;

      if (response.success) {
        setWishlist(response.data || []);
      }
    } catch (error) {
      console.error("Error fetching wishlist:", error);
    }
  };

  const handleAddDialogOpen = () => {
    setAddDialogOpen(true);
  };

  const handleAddDialogClose = () => {
    setAddDialogOpen(false);
  };

  const handleEditDialogOpen = (wish: Wish) => {
    setSelectedWish(wish);
    setEditDialogOpen(true);
  };

  const handleEditDialogClose = () => {
    setSelectedWish(null);
    setEditDialogOpen(false);
  };

  const handleNewWishNameChange = (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    setNewWishName(event.target.value);
  };

  const handleAddWish = async () => {
    try {
      const { data } = await axios.post(`wishlist`, {
        name: newWishName,
        authors: newWishAuthors.split(","),
      });
      const response: ApiResponse<Wish> = data;

      if (response.success) {
        setWishlist([...wishlist, response.data!]);
        setNewWishName("");
        toast.success("Wish added successfully");
        handleAddDialogClose();
      } else {
        toast.error(response.message ?? "Failed to add wish");
      }
    } catch (error) {
      console.error("Error adding wish:", error);
    }
  };

  const handleEditWish = async () => {
    if (!selectedWish) return;

    try {
      const { data } = await axios.put(
        `wishlist`,
        JSON.stringify(selectedWish)
      );
      const response: ApiResponse<Wish> = data;

      if (response.success) {
        const updatedWishlist = wishlist.map((wish) =>
          wish.id === response.data?.id ? response.data! : wish
        );
        setWishlist(updatedWishlist);
        toast.success("Wish updated successfully");
        handleEditDialogClose();
      } else {
        toast.error(response.message ?? "Failed to update wish");
      }
    } catch (error) {
      console.error("Error updating wish:", error);
    }
  };

  const handleDeleteWish = async (id: number) => {
    try {
      const { data } = await axios.delete(`wishlist/${id}`);
      const response: ApiResponse<boolean> = data;

      if (response.success) {
        const updatedWishlist = wishlist.filter((wish) => wish.id !== id);
        setWishlist(updatedWishlist);
        toast.success("Wish deleted successfully");
      } else {
        toast.error(response.message ?? "Failed to delete wish");
      }
    } catch (error) {
      console.error("Error deleting wish:", error);
    }
  };

  const handleNewWishAuthorsChange = (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    setNewWishAuthors(event.target.value);
  };

  return (
    <>
      <div>
        <UserHeader />
        <div className="container mx-auto mt-8">
          <div>
            <Button
              variant="contained"
              color="primary"
              onClick={handleAddDialogOpen}
            >
              Add Wishlist
            </Button>

            {wishlist.map((wish) => (
              <Card key={wish.id} style={{ marginTop: "16px" }}>
                <CardContent>
                  <Typography variant="h6">{wish.name}</Typography>
                  <div>
                    <Typography variant="subtitle2">Authors:</Typography>
                    {wish.authors.map((author, index) => (
                      <Chip
                        key={index}
                        label={author}
                        style={{ marginRight: "8px" }}
                      />
                    ))}
                  </div>
                </CardContent>
                <CardActions>
                  <Button
                    size="small"
                    onClick={() => handleEditDialogOpen(wish)}
                  >
                    Edit
                  </Button>
                  <Button
                    size="small"
                    onClick={() => handleDeleteWish(wish.id)}
                  >
                    Delete
                  </Button>
                </CardActions>
              </Card>
            ))}
            {wishlist.length === 0 && (
              <div className="mt-8">
                <Typography variant="body1" color="textSecondary">
                  You haven't added any wishlist yet.
                </Typography>
              </div>
            )}

            <Dialog open={isAddDialogOpen} onClose={handleAddDialogClose}>
              <DialogTitle>Add Wishlist</DialogTitle>
              <DialogContent>
                <TextField
                  label="Wish Name"
                  variant="outlined"
                  fullWidth
                  value={newWishName}
                  onChange={handleNewWishNameChange}
                />
                <TextField
                  label="Authors (comma-separated)"
                  variant="outlined"
                  fullWidth
                  value={newWishAuthors}
                  onChange={handleNewWishAuthorsChange}
                  sx={{ marginTop: "12px" }}
                />
              </DialogContent>
              <DialogActions>
                <Button onClick={handleAddDialogClose} color="primary">
                  Cancel
                </Button>
                <Button onClick={handleAddWish} color="primary">
                  Add
                </Button>
              </DialogActions>
            </Dialog>

            <Dialog open={isEditDialogOpen} onClose={handleEditDialogClose}>
              <DialogTitle>Edit Wishlist</DialogTitle>
              <DialogContent>
                <TextField
                  label="Wish Name"
                  variant="outlined"
                  fullWidth
                  value={selectedWish?.name || ""}
                  onChange={(e) =>
                    setSelectedWish((prevWish) => {
                      if (!prevWish) {
                        return null;
                      }
                      return {
                        ...prevWish,
                        name: e.target.value,
                      };
                    })
                  }
                />
                <TextField
                  label="Authors (comma-separated)"
                  variant="outlined"
                  fullWidth
                  value={selectedWish?.authors.join(", ") || ""}
                  onChange={(e) =>
                    setSelectedWish((prevWish) => {
                      if (!prevWish) {
                        return null;
                      }
                      return {
                        ...prevWish,
                        authors: e.target.value.split(","),
                      };
                    })
                  }
                  sx={{ marginTop: "12px" }}
                />
              </DialogContent>
              <DialogActions>
                <Button onClick={handleEditDialogClose} color="primary">
                  Cancel
                </Button>
                <Button onClick={handleEditWish} color="primary">
                  Save
                </Button>
              </DialogActions>
            </Dialog>
          </div>
        </div>
      </div>
    </>
  );
}
