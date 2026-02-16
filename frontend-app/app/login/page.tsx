import LoginForm from "./LoginForm";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Login",
  description: "Login to your account to track your progress"
}

export default function LoginPage() {
  return <LoginForm />;
}