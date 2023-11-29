import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../AuthProvider";
import { Avatar, AvatarFallback, AvatarImage } from "../ui/avatar";
import {
  NavigationMenu,
  NavigationMenuContent,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
  NavigationMenuTrigger,
} from "../ui/navigation-menu";
import { LogoutRounded, Person2Rounded } from "@mui/icons-material";

const UserHeader = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  return (
    <header className="bg-blue-500 text-white py-4">
      <div className="container mx-auto flex justify-between items-center">
        <Link to={"/home"}>
          <h1 className="text-2xl font-bold">ShareBook</h1>
        </Link>

        <div className="flex justify-center items-center">
          <Link className="mr-10 hover:underline" to={"/my-books"}>
            My Books
          </Link>

          <Link to={"/user"}>
            <Avatar className="mr-4">
              <AvatarImage src="https://github.com/shadcn.png" />
              <AvatarFallback className="text-gray-900">
                {user.name.charAt(0)}
              </AvatarFallback>
            </Avatar>
          </Link>

          <NavigationMenu className="mr-8 text-black">
            <NavigationMenuList className="py-4">
              <NavigationMenuItem>
                <NavigationMenuTrigger className="py-4">
                  <div className="flex justify-center items-center">
                    <p className="mr-2">{user.name}</p>
                  </div>
                </NavigationMenuTrigger>
                <NavigationMenuContent>
                  <div className="w-36 h-40 bg-white rounded-sm flex flex-col">
                    <div className="flex-grow">
                      <NavigationMenuLink
                        className="w-full h-10 hover:bg-slate-200 flex justify-start items-center px-2 cursor-pointer"
                        onClick={() => {
                          navigate("/user");
                        }}
                      >
                        <div className="flex">
                          <Person2Rounded className="mr-2" />
                          <p>Profile</p>
                        </div>
                      </NavigationMenuLink>
                    </div>

                    <NavigationMenuLink
                      className="w-full h-10 hover:bg-slate-200 flex justify-start items-center px-2 cursor-pointer flex-shrink-0"
                      onClick={() => {
                        logout();
                      }}
                    >
                      <div className="flex">
                        <LogoutRounded color="warning" className="mr-2" />
                        <p className="text-red-600">Logout</p>
                      </div>
                    </NavigationMenuLink>
                  </div>
                </NavigationMenuContent>
              </NavigationMenuItem>
            </NavigationMenuList>
          </NavigationMenu>
        </div>
      </div>
    </header>
  );
};

export default UserHeader;
