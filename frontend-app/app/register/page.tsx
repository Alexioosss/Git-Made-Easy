import { Metadata } from "next"
import RegisterForm from "./RegisterForm"

export const metadata: Metadata = {
  title: "Create Your Account",
  description: "Register to save your progress and track your learning journey"
};

export default function RegisterPage() {
  return <RegisterForm />;
}