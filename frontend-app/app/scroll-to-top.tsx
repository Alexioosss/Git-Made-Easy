"use client"

import { usePathname } from "next/navigation";
import { useEffect } from "react";

/**
 * ScrollToTop
 * 
 * A utility component that automatically scrolls the page to the top whenever the route changes.
 * In Next.js Apps, page transitions preserve the last scroll position.
 * 
 * Usage: To place inside a page layout, i.e. root layout, so it runs on every route transition across the entire application.
 * 
 * @returns null - This component does not require returns since it does not render anything visual.
 */
export function ScrollToTop() {
    const pathName = usePathname(); // Grab the current path name, i.e. /lessons
    useEffect(() => {
        window.scrollTo({ top: 0, behavior: "smooth" }); // Scroll to the top of the page with a smooth animation / motion
    }, [pathName]); // Happens every time the pathName changes, i.e. from /lessons to /lessons/1
    return null;
}