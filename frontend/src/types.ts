export type IProps = React.PropsWithChildren<{ children: JSX.Element }>;

export type ApiResponse<T> = {
  success: boolean;
  message?: null;
  data?: T;
};

export type User = {
  id: number;
  name: string;
  email: string;
  profilePictureUrl?: string;
  gender?: "Male" | "Female";
  age?: number;
};

export type Book = {
  id: number;
  name: string;
  authors: string[];
  genre: string[];
  coverImage: string;
  sampleImages: string[];
  available: boolean;
  swappable: boolean;
  price: number;
  user?: User;
};

export type Review = {
  id: number;
  user: User;
  rating: number;
  review: string;
};

export type BookRequest = {
  id: number;
  book: Book;
  user: User;
  swapBook?: Book;
  createdAt?: string;
  updatedAt?: string;
  approved: boolean;
  rejected: boolean;
};
