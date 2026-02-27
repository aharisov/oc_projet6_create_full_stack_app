export interface Post {
  id: number;
  topicId: number;
  topicName: string | null;
  authorId: number;
  authorName: string | null;
  title: string;
  content: string;
  createdAt: string;
  updatedAt: string;
}
