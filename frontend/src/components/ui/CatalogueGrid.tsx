"use client";

import React, { useEffect, useId, useRef, useState } from "react";
import { AnimatePresence, motion } from "motion/react";
import { useOutsideClick } from "@/hooks/use-outside-click";
import type { Listing } from "@/types/listing";
import type { VehicleImage } from "@/types/vehicle";
import { useGetListings } from "@/hooks/useGetListings";
import Loading from "@/components/ui/Loading";
import { VehicleImageGallery } from "./VehicleImageGallery";

export default function CatalogueGrid() {
  const [active, setActive] = useState<Listing | null>(null);
  const id = useId();
  const ref = useRef<HTMLDivElement>(null);
  const { listings, isLoading, error } = useGetListings();

  const getImageUrl = (image?: VehicleImage): string | null | undefined => {
    if (!image) {
      return null;
    }

    if (image.data && typeof image.data === "string") {
      const mimeType = image.mimeType;
      return `data:${mimeType};base64,${image.data}`;
    }
  };

  useEffect(() => {
    function onKeyDown(event: KeyboardEvent) {
      if (event.key === "Escape") {
        setActive(null);
      }
    }

    if (active && typeof active === "object") {
      document.body.style.overflow = "hidden";
    } else {
      document.body.style.overflow = "auto";
    }

    window.addEventListener("keydown", onKeyDown);

    return () => window.removeEventListener("keydown", onKeyDown);
  }, [active]);

  useOutsideClick(ref, () => setActive(null));

  if (isLoading) {
    return (
      <div className="w-full h-full flex items-center justify-center">
        <Loading />
      </div>
    );
  }

  if (error) {
    return (
      <div className="w-full h-full flex items-center justify-center">
        <div className="text-red-500">
          Error loading listings: {error.message}
        </div>
      </div>
    );
  }

  return (
    <>
      <AnimatePresence>
        {active && typeof active === "object" && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            className="fixed inset-0 bg-black/20 h-full w-full z-10"
          />
        )}
      </AnimatePresence>
      <AnimatePresence>
        {active && typeof active === "object" ? (
          <div className="fixed inset-0  grid place-items-center z-[100]">
            <motion.button
              key={`button-${active.title}-${id}`}
              layout
              initial={{
                opacity: 0,
              }}
              animate={{
                opacity: 1,
              }}
              exit={{
                opacity: 0,
                transition: {
                  duration: 0.05,
                },
              }}
              className="flex absolute top-2 right-2 lg:hidden items-center justify-center bg-white rounded-full h-6 w-6"
              onClick={() => setActive(null)}
            >
              <CloseIcon />
            </motion.button>

            <motion.div
              layoutId={`card-${active.title}-${id}`}
              ref={ref}
              className="w-full max-w-[500px] h-full md:h-fit md:max-h-[90%] flex flex-col bg-white sm:rounded-3xl overflow-y-auto"
            >
              <div className="p-4">
                <VehicleImageGallery vehicle={active.vehicle} />
              </div>

              <div>
                <div className="flex justify-between items-start p-4">
                  <div className="">
                    <motion.h3
                      layoutId={`title-${active.title}-${active.id}`}
                      className="font-medium text-neutral-700 "
                    >
                      {active.title}
                    </motion.h3>
                    <motion.p
                      layoutId={`description-${active.vehicle.make} ${active.vehicle.model} ${active.vehicle.year} - ${active.id}`}
                      className="text-neutral-600  text-base"
                    >
                      {`${active.vehicle.make} ${active.vehicle.model} ${active.vehicle.year}`}
                    </motion.p>
                  </div>

                  <motion.a
                    layout
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    exit={{ opacity: 0 }}
                    href={"/app/listings/" + active.id}
                    target="_blank"
                    className="px-4 py-3 text-sm rounded-full font-bold bg-green-500 text-white"
                  >
                    Book Now
                  </motion.a>
                </div>
                <div className="pt-4 relative px-4">
                  <motion.div
                    layout
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    exit={{ opacity: 0 }}
                    className="text-neutral-600 text-xs md:text-sm lg:text-base h-40 md:h-fit pb-10 flex flex-col items-start gap-4 overflow-auto dark:text-neutral-400 [mask:linear-gradient(to_bottom,white,white,transparent)] [scrollbar-width:none] [-ms-overflow-style:none] [-webkit-overflow-scrolling:touch]"
                  >
                    {active.description}
                  </motion.div>
                </div>
              </div>
            </motion.div>
          </div>
        ) : null}
      </AnimatePresence>
      <ul className="w-full grid grid-cols-1 md:grid-cols-3 items-start gap-4">
        {listings?.map((listing) => (
          <motion.div
            layoutId={`card-${listing.title}-${id}`}
            key={listing.title}
            onClick={() => setActive(listing)}
            className="p-4 flex flex-col bg-[#f8fafcac] hover:bg-neutral-50 rounded-xl cursor-pointer shadow-sm"
          >
            <div className="flex gap-4 flex-col  w-full">
              <motion.div
                ref={ref}
                className="w-full max-w-[500px] h-full md:h-fit md:max-h-[90%] flex flex-col bg-white sm:rounded-3xl overflow-y-auto"
              >
                {(() => {
                  const imageUrl = getImageUrl(listing.vehicle.images?.[0]);
                  const placeholderUrl =
                    "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='400' height='300'%3E%3Crect fill='%23ddd' width='400' height='300'/%3E%3Ctext fill='%23999' font-family='sans-serif' font-size='18' dy='10.5' font-weight='bold' x='50%25' y='50%25' text-anchor='middle'%3EImage not found%3C/text%3E%3C/svg%3E";

                  return (
                    <img
                      width={200}
                      height={200}
                      src={imageUrl ?? placeholderUrl}
                      alt={listing.title}
                      className="w-full h-48 lg:h-48 sm:rounded-tr-lg sm:rounded-tl-lg object-cover object-top"
                    />
                  );
                })()}
              </motion.div>
              <div className="flex justify-center items-center flex-col">
                <motion.h3
                  layoutId={`title-${listing.title}-${id}`}
                  className="font-medium text-neutral-800  text-center md:text-left text-base"
                >
                  {listing.title}
                </motion.h3>
                <motion.p
                  layoutId={`description-${listing.vehicle.make} ${listing.vehicle.model} ${listing.vehicle.year} - ${id}`}
                  className="text-neutral-600 text-base"
                >
                  {`${listing.vehicle.make} ${listing.vehicle.model} ${listing.vehicle.year}`}
                </motion.p>
              </div>
            </div>
          </motion.div>
        ))}
      </ul>
    </>
  );
}

export const CloseIcon = () => {
  return (
    <motion.svg
      initial={{
        opacity: 0,
      }}
      animate={{
        opacity: 1,
      }}
      exit={{
        opacity: 0,
        transition: {
          duration: 0.05,
        },
      }}
      xmlns="http://www.w3.org/2000/svg"
      width="24"
      height="24"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
      className="h-4 w-4 text-black"
    >
      <path stroke="none" d="M0 0h24v24H0z" fill="none" />
      <path d="M18 6l-12 12" />
      <path d="M6 6l12 12" />
    </motion.svg>
  );
};
