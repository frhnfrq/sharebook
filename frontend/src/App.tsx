import { Route, Routes } from "react-router-dom";
import HomePage from "./pages/HomePage";
import ProtectedPage from "./pages/ProtectedPage";
import UserPage from "./pages/UserPage";
import LoginPage from "./pages/LoginPage";
import LandingPage from "./pages/LandingPage";
import RegisterPage from "./pages/RegisterPage";
import MyBooksPage from "./pages/MyBooksPage";
import { useEffect, useState } from "react";
import { Toaster } from "react-hot-toast";
import SearchPage from "./pages/SearchPage";
import BookDetailsPage from "./pages/BookDetailsPage";
import BookExchangesPage from "./pages/BookExchangesPage";

function App() {
  const [isMount, setMount] = useState(false);

  useEffect(() => {
    setMount(true);
  }, []);

  return (
    <div>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route
          path="/home"
          element={
            <ProtectedPage>
              <HomePage />
            </ProtectedPage>
          }
        />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route
          path="/user"
          element={
            <ProtectedPage>
              <UserPage />
            </ProtectedPage>
          }
        />
        <Route
          path="/my-books"
          element={
            <ProtectedPage>
              <MyBooksPage />
            </ProtectedPage>
          }
        />
        <Route
          path="/search"
          element={
            <ProtectedPage>
              <SearchPage />
            </ProtectedPage>
          }
        />
        <Route
          path="/books/:id"
          element={
            <ProtectedPage>
              <BookDetailsPage />
            </ProtectedPage>
          }
        />
        <Route
          path="/book-exchanges"
          element={
            <ProtectedPage>
              <BookExchangesPage />
            </ProtectedPage>
          }
        />
      </Routes>

      {isMount && <Toaster />}
    </div>
  );
}

export default App;
