import { Route, Routes } from "react-router-dom";
import HomePage from "./pages/HomePage";
import ProtectedPage from "./pages/ProtectedPage";
import UserPage from "./pages/UserPage";
import LoginPage from "./pages/LoginPage";
import LandingPage from "./pages/LandingPage";
import RegisterPage from "./pages/RegisterPage";
import MyBooksPage from "./pages/MyBooksPage";

function App() {
  return (
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
    </Routes>
  );
}

export default App;
