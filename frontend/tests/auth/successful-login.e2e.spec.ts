import { test } from "@playwright/test";

test("test", async ({ page }) => {
  await page.goto("http://localhost:5173/");
  await page.locator("#email").click();
  await page.locator("#email").fill("ivelinvasilev4040@gmail.com");
  await page.locator("#password").click();
  await page.locator("#password").fill("#03102005Ivelin");
  await page.getByRole("button", { name: "Sign In" }).click();
});
