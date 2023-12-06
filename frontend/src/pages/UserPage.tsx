import { ChangeEvent, useEffect, useState } from "react";
import UserHeader from "../components/custom/UserHeader";
import { ApiResponse, User } from "../types";
import { useAuth } from "../AuthProvider";
import axios, { imageBaseURL } from "../network/axios";
import toast from "react-hot-toast";
import {
  Button,
  Card,
  CardContent,
  CardMedia,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControl,
  FormControlLabel,
  FormLabel,
  IconButton,
  Input,
  InputLabel,
  Paper,
  Switch,
  Typography,
} from "@mui/material";
import { CloseRounded } from "@mui/icons-material";

export default function UserPage() {
  const authState = useAuth();

  const [isEditDialogOpen, setEditDialogOpen] = useState(false);
  const [editedUser, setEditedUser] = useState<User>(authState.user);

  useEffect(() => {
    setEditedUser(authState.user);
  }, [authState.user]);

  const handleEditDialogOpen = () => {
    setEditDialogOpen(true);
  };

  const handleEditDialogClose = () => {
    setEditDialogOpen(false);
  };

  const handleInputChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setEditedUser((prevUser) => ({ ...prevUser, [name]: value }));
  };

  const handleSwitchChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, checked } = event.target;
    setEditedUser((prevUser) => ({ ...prevUser, [name]: checked }));
  };

  const handleFileChange = async (event: ChangeEvent<HTMLInputElement>) => {
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

        if (!data.success) {
          toast.error(data.message);
          return;
        }

        setEditedUser((prevUser) => ({
          ...prevUser,
          profilePicture: data.data,
        }));
      } catch (error) {
        console.error("Error uploading image:", error);
        // Handle error, e.g., show a message to the user
      }
    }
  };

  const handleRemoveProfilePicture = () => {
    setEditedUser((prevUser) => ({ ...prevUser, profilePicture: "" }));
  };

  const handleSaveChanges = async () => {
    try {
      const { data } = await axios.put("user", JSON.stringify(editedUser));
      const response: ApiResponse<User> = data;

      if (response.success) {
        toast.success("User details updated successfully");
      } else {
        toast.error(response.message ?? "Failed to update user details");
      }
    } catch (error) {
      console.error("Error updating user details:", error);
    }

    setEditDialogOpen(false);
  };

  return (
    <>
      <div>
        <UserHeader />
        <div className="container">
          <Card>
            <CardContent>
              <div className="flex">
                <img
                  alt=""
                  src={
                    editedUser.profilePicture
                      ? imageBaseURL + editedUser.profilePicture
                      : "https://placekitten.com/200/300"
                  }
                  height={300}
                  width={200}
                  className="rounded-sm"
                ></img>
                <div className="flex flex-col ml-8">
                  <Typography variant="h6" gutterBottom>
                    Name: {editedUser.name}
                  </Typography>
                  <Typography variant="body1" color="textSecondary">
                    Email: {editedUser.email}
                  </Typography>
                  <Typography variant="body1" color="textSecondary">
                    Address: {editedUser.address}
                  </Typography>
                  <Button
                    variant="contained"
                    color="primary"
                    onClick={handleEditDialogOpen}
                  >
                    Edit Details
                  </Button>
                </div>
              </div>
            </CardContent>
          </Card>

          <Dialog
            open={isEditDialogOpen}
            onClose={handleEditDialogClose}
            fullWidth
            maxWidth="md"
          >
            <DialogTitle>Edit User Details</DialogTitle>
            <DialogContent>
              <FormControl fullWidth margin="normal">
                <InputLabel htmlFor="name">Name</InputLabel>
                <Input
                  id="name"
                  name="name"
                  value={editedUser.name}
                  onChange={handleInputChange}
                />
              </FormControl>

              <FormControl fullWidth margin="normal">
                <InputLabel htmlFor="email">Email</InputLabel>
                <Input
                  id="email"
                  name="email"
                  value={editedUser.email}
                  onChange={handleInputChange}
                  disabled
                />
              </FormControl>

              <FormControl fullWidth margin="normal">
                <InputLabel htmlFor="address">Address</InputLabel>
                <Input
                  id="address"
                  name="address"
                  value={editedUser.address}
                  onChange={handleInputChange}
                />
              </FormControl>

              <FormLabel>Profile Picture</FormLabel>

              {editedUser.profilePicture && (
                <Paper
                  elevation={3}
                  style={{
                    position: "relative",
                    maxWidth: 200,
                    margin: "10px",
                  }}
                >
                  <img
                    src={imageBaseURL + editedUser.profilePicture}
                    alt="Profile Preview"
                    style={{ maxWidth: "100%", height: "auto" }}
                  />
                  <IconButton
                    size="small"
                    color="error"
                    onClick={handleRemoveProfilePicture}
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
                type="file"
                id="profilePicture"
                name="profilePicture"
                onChange={handleFileChange}
              />
            </DialogContent>
            <DialogActions>
              <Button onClick={handleEditDialogClose} color="primary">
                Cancel
              </Button>
              <Button onClick={handleSaveChanges} color="primary">
                Save Changes
              </Button>
            </DialogActions>
          </Dialog>
        </div>
      </div>
    </>
  );
}
