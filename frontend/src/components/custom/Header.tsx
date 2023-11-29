import React from "react";
import { Button } from "@mui/joy";
import { useAuthBase } from "../../AuthProvider";
import { Link } from "react-router-dom";

const Header = () => {
  const { user } = useAuthBase();

  return (
    <header className="bg-blue-500 text-white py-4">
      <div className="container mx-auto flex justify-between items-center">
        <h1 className="text-2xl font-bold">ShareBook</h1>
        <div>
          {user ? (
            <Link to="/home">
              <Button variant="soft" color="primary">
                Go to Home
              </Button>
            </Link>
          ) : (
            <>
              <Link to="/login" className="mr-2">
                <Button>Login</Button>
              </Link>
              <Link to="/register">
                <Button variant="soft" color="primary">
                  Register
                </Button>
              </Link>
            </>
          )}
        </div>
      </div>
    </header>
  );
};

export default Header;
