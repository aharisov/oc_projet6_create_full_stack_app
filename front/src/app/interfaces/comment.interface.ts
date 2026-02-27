export interface Comment {
  id: number;
  postId: number;
  authorId: number;
  authorName: string | null;
  content: string;
  createdAt: string;
}
