import { Button } from "@mui/joy";
import { useAuth } from "../AuthProvider";
import UserHeader from "../components/custom/UserHeader";

export default function HomePage() {
  const authState = useAuth();

  return (
    <>
      <div>
        <UserHeader />
      </div>
    </>
  );
}
