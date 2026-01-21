import { test as setup, expect } from "@playwright/test";

const ownerAuthFile = "playwright/.auth/owner.json";
const renterAuthFile = "playwright/.auth/renter.json";

setup("authenticate as owner", async ({ page }) => {
  await page.goto("http://localhost:5173/");
  await page.locator("#email").click();
  await page.locator("#email").fill("cobatabg@gmail.com");
  await page.locator("#password").click();
  await page.locator("#password").fill("#03102005Ivelin");
  await page.getByRole("button", { name: "Sign In" }).click();
  await page.waitForURL("**/app/**");
  await page.context().storageState({ path: ownerAuthFile });
});

setup("authenticate as renter", async ({ page }) => {
  await page.goto("http://localhost:5173/");
  await page.locator("#email").click();
  await page.locator("#email").fill("ivelinvasilev4040@gmail.com");
  await page.locator("#password").click();
  await page.locator("#password").fill("#03102005Ivelin");
  await page.getByRole("button", { name: "Sign In" }).click();
  await page.waitForURL("**/app/**");
  await page.context().storageState({ path: renterAuthFile });
});
