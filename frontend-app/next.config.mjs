/** @type {import('next').NextConfig} */
const nextConfig = {
  typescript: {
    ignoreBuildErrors: true,
  },
  images: {
    unoptimized: true,
  },
  devIndicators: {
    icon: false,
  },
  env: {
    NEXT_PUBLIC_API_URL: "http://localhost:8080/"
  }
}

export default nextConfig