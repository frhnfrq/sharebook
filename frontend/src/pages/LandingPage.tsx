import { Button } from "@mui/joy";
import { useNavigate } from "react-router-dom";
import Header from "../components/custom/Header";

const cardData = [
  {
    title: "Share with Others",
    description:
      "Share your favorite books with the community and discover new ones.",
    imageUrl: "https://placekitten.com/400/300", // Replace with actual image URL
  },
  {
    title: "Exchange Books",
    description:
      "Connect with other readers for book exchanges and expand your library.",
    imageUrl: "https://placekitten.com/401/300", // Replace with actual image URL
  },
  {
    title: "Earn Money",
    description:
      "Sell your old books and make money while helping others find affordable reads.",
    imageUrl: "https://placekitten.com/402/300", // Replace with actual image URL
  },
];

export default function LandingPage() {
  const navigate = useNavigate();

  return (
    <>
      <div className="bg-gray-100 font-sans">
        <Header />
        <div className="container mx-auto mt-8 text-center">
          <div className="flex flex-col justify-center items-center mb-8 h-[400px]">
            <h2 className="text-5xl font-bold mb-4">
              Share Your Love for Books
            </h2>
            <p className="text-lg text-gray-700 mb-8">
              Connect with a community of book lovers. Share, exchange, and
              discover new reads.
            </p>
            <Button
              variant="solid"
              color="primary"
              onClick={() => navigate("/login")}
            >
              Join Now
            </Button>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {cardData.map((card, index) => (
              <div
                key={index}
                className="relative bg-white rounded-lg overflow-hidden shadow-md transition-transform transform hover:scale-105"
              >
                <img
                  src={card.imageUrl}
                  alt={card.title}
                  className="w-full h-80 object-cover object-center rounded-t-lg"
                />
                <div className="p-6">
                  <h3 className="text-xl font-bold mb-2">{card.title}</h3>
                  <p className="text-gray-700">{card.description}</p>
                </div>
              </div>
            ))}
          </div>
          <section className="my-32 text-center">
            <div className="max-w-3xl mx-auto">
              <h2 className="text-4xl font-bold mb-4">About Us</h2>
              <p className="text-gray-700 mb-4">
                ShareBook is more than just a platform; it's a vibrant community
                where book enthusiasts come together to share their passion for
                reading. Whether you're looking to share your favorite books,
                exchange with fellow readers, or earn from your old books,
                ShareBook is the place for you.
              </p>
              <p className="text-gray-700">
                Our mission is to make reading accessible and enjoyable for
                everyone. Join us on this literary journey and be a part of a
                community that celebrates the magic of storytelling.
              </p>
            </div>
          </section>
        </div>
        <footer className="bg-gray-800 text-white py-8">
          <div className="container mx-auto text-center">
            <p className="text-sm">
              &copy; {new Date().getFullYear()} ShareBook. All rights reserved.
            </p>
            <p className="text-sm">Designed with ❤️ by ShareBook Team</p>
          </div>
        </footer>
      </div>
    </>
  );
}
