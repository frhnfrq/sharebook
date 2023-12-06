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
  profilePicture?: string;
  address?: string;
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

export type BookExchange = {
  id: number;
  book: Book;
  swapBook?: Book;
  bookOwnerUser: User;
  bookRenterUser: User;
  createdAt: string;
  dueAt: string;
  returned: boolean;
  price: number;
  bookRequest: BookRequest;
};
