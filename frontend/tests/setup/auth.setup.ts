import { test } from "@playwright/test";

test("authenticate as renter", async ({ page }) => {
  await page.goto("http://localhost:5173/");

  await page.getByLabel("Email").fill("ivanov123@gmail.com");
  await page.getByLabel("Password").fill("#03102005Ivelin");
  await page.getByRole("button", { name: "Login" }).click();

  await page.waitForURL("/dashboard");

  await page.context().storageState({
    path: "playwright/.auth/renter.json",
  });
});

test("authenticate as owner", async ({ page }) => {
  await page.goto("http://localhost:5173/");

  await page.getByLabel("Email").fill("ivelinvasilev4038@gmail.com");
  await page.getByLabel("Password").fill("#03102005Ivelin");
  await page.getByRole("button", { name: "Login" }).click();

  await page.waitForURL("/owner");

  await page.context().storageState({
    path: "playwright/.auth/owner.json",
  });
});
