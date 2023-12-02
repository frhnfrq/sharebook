import axios from "axios";

const baseURL = "http://localhost:8080/api/";
export const imageBaseURL = `${baseURL}image/`;

export default axios.create({
  baseURL,
  headers: {
    "Content-Type": "application/json",
    Authorization: `Bearer ${localStorage.getItem("token")}`,
  },
});
