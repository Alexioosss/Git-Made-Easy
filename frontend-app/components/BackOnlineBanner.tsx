"use client"

import { useAuth } from "@/context/AuthContext"
import { useEffect, useRef, useState } from "react";

export function BackOnlineBanner() {
    const isServerAvailable = useAuth().isServerAvailable;
    const [show, setShow] = useState(false);
    const previousServerState = useRef<boolean | null>(null);

    useEffect(() => {
        if(previousServerState.current === null) {
            previousServerState.current = isServerAvailable;
            return;
        }

        if(previousServerState.current === false && isServerAvailable === true) {
            setShow(true);
            const timeout = setTimeout(() => setShow(false), 3000);
            previousServerState.current = isServerAvailable;
            return () => clearTimeout(timeout);
        }
        previousServerState.current = isServerAvailable;
    }, [isServerAvailable]);
    
    if(!show) { return null; }

    return (
        <div className="sticky top-[56px] md:top-[64px] z-50 w-full bg-green-600 text-white text-center py-2 text-sm font-medium">
            Woohoo! We are back online. Your changes will now sync.
        </div>
    );
}