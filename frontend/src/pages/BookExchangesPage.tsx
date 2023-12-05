import React, { useEffect, useState } from "react";
import {
  Tab,
  Tabs,
  Card,
  CardContent,
  Button,
  Typography,
  Grid,
} from "@mui/material";
import axios, { imageBaseURL } from "../network/axios";
import { ApiResponse, Book, User, BookExchange, BookRequest } from "../types";
import UserHeader from "../components/custom/UserHeader";
import toast from "react-hot-toast";

const BookExchangesPage: React.FC = () => {
  const [ownerExchanges, setOwnerExchanges] = useState<BookExchange[]>([]);
  const [renterExchanges, setRenterExchanges] = useState<BookExchange[]>([]);
  const [value, setValue] = useState(0);

  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };

  const handleReturnBook = async (exchangeId: number) => {
    try {
      const { data } = await axios.post(`books/exchanges/return/${exchangeId}`);
      if (data.success) {
        setRenterExchanges((exchanges) =>
          exchanges.map((exchange) =>
            exchange.id === exchangeId
              ? { ...exchange, returned: true }
              : exchange
          )
        );
        toast.success("Returned the book successfully");
      } else {
        toast.error(data.message ?? "Something went wrong");
      }
    } catch (error) {
      console.error("Error returning book:", error);
    }
  };

  useEffect(() => {
    const fetchExchanges = async () => {
      try {
        const ownerResponse = await axios.get("books/exchanges/owner");
        const renterResponse = await axios.get("books/exchanges/renter");

        const ownerExchangesData: ApiResponse<BookExchange[]> =
          ownerResponse.data;
        const renterExchangesData: ApiResponse<BookExchange[]> =
          renterResponse.data;

        if (ownerExchangesData.success) {
          setOwnerExchanges(ownerExchangesData.data || []);
        }

        if (renterExchangesData.success) {
          setRenterExchanges(renterExchangesData.data || []);
        }
      } catch (error) {
        console.error("Error fetching book exchanges:", error);
      }
    };

    fetchExchanges();
  }, []);

  return (
    <div>
      <UserHeader />
      <div className="container mx-auto mt-8">
        <div className="mb-8">
          <Typography variant="h5" color="textSecondary">
            Your book exchanges
          </Typography>
        </div>

        <Tabs value={value} onChange={handleChange}>
          <Tab label="As Owner" />
          <Tab label="As Renter" />
        </Tabs>

        {/* Owner Exchanges Tab */}
        {value === 0 && (
          <div>
            {ownerExchanges.map((exchange) => (
              <div key={exchange.id} className="flex flex-col m-12">
                <Card>
                  <CardContent>
                    <div className="flex">
                      <img
                        alt=""
                        src={imageBaseURL + exchange.book.coverImage}
                        height={300}
                        width={200}
                        className="rounded-sm"
                      ></img>

                      <div className="flex flex-col ml-4">
                        <Typography variant="h5" gutterBottom>
                          {exchange.book.name}
                        </Typography>
                        <Typography
                          variant="subtitle1"
                          color="textSecondary"
                          mb={2}
                        >
                          Rented to: {exchange.bookRenterUser.name}
                        </Typography>
                        <Typography
                          variant="subtitle1"
                          color="textSecondary"
                          mb={2}
                        >
                          Due Date:{" "}
                          {new Date(exchange.dueAt).toLocaleDateString()}
                        </Typography>

                        {exchange.returned && (
                          <Typography
                            variant="subtitle1"
                            color="textSecondary"
                            mb={2}
                          >
                            Returned
                          </Typography>
                        )}
                      </div>
                    </div>
                  </CardContent>
                </Card>
              </div>
            ))}

            {ownerExchanges.length === 0 && (
              <div className="mt-12 ml-12">
                <Typography variant="body1" color="textSecondary">
                  You have not loaned your books yet.
                </Typography>
              </div>
            )}
          </div>
        )}

        {/* Renter Exchanges Tab */}
        {value === 1 && (
          <div>
            {renterExchanges.map((exchange) => (
              <div key={exchange.id} className="flex flex-col m-12">
                <Card>
                  <CardContent>
                    <div className="flex">
                      <img
                        alt=""
                        src={imageBaseURL + exchange.book.coverImage}
                        height={300}
                        width={200}
                        className="rounded-sm"
                      ></img>

                      <div className="flex flex-col ml-4">
                        <Typography variant="h5" gutterBottom>
                          {exchange.book.name}
                        </Typography>
                        <Typography
                          variant="subtitle1"
                          color="textSecondary"
                          mb={2}
                        >
                          Rented to: {exchange.bookRenterUser.name}
                        </Typography>
                        <Typography
                          variant="subtitle1"
                          color="textSecondary"
                          mb={2}
                        >
                          Due Date:{" "}
                          {new Date(exchange.dueAt).toLocaleDateString()}
                        </Typography>

                        {exchange.returned && (
                          <Typography
                            variant="subtitle1"
                            color="textSecondary"
                            mb={2}
                          >
                            Returned
                          </Typography>
                        )}

                        {!exchange.returned && (
                          <Button
                            variant="contained"
                            color="primary"
                            onClick={() => handleReturnBook(exchange.id)}
                          >
                            Return
                          </Button>
                        )}
                      </div>
                    </div>
                  </CardContent>
                </Card>
              </div>
            ))}

            {renterExchanges.length === 0 && (
              <div className="mt-12 ml-12">
                <Typography variant="body1" color="textSecondary">
                  You have not rented any books yet.
                </Typography>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default BookExchangesPage;
