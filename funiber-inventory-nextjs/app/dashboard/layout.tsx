import DashBoardSideBar from "@/components/dashboard/DashBoardSideBar";
import React from "react";

export default function RootLayout({
  children,
}: Readonly<{ children: React.ReactNode }>) {
  return (
    <>
      <div className="md:flex">
        <DashBoardSideBar category={""}></DashBoardSideBar>
        <main>{children}</main>
      </div>
    </>
  );
}
