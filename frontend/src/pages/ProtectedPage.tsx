import CircularProgress from "@mui/joy/CircularProgress";
import { Navigate } from "react-router-dom";
import { useAuthBase } from "../AuthProvider";

export default function ProtectedPage({
  children,
}: React.PropsWithChildren<{ children: JSX.Element }>) {
  const { user, loading } = useAuthBase();

  if (loading) {
    return (
      <>
        <div className="flex items-center justify-center h-screen">
          <CircularProgress />
        </div>
      </>
    );
  } else if (!user) {
    return (
      <>
        <Navigate to={"/login"} />
      </>
    );
  } else {
    return children;
  }
}
