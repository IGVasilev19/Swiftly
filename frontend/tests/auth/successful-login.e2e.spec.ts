import { test, expect } from "@playwright/test";

test("successful login shows a success message", async ({ page }) => {
  await page.goto("http://localhost:5173/");

  await page.locator("#email").fill("cobatabg@gmail.com");
  await page.locator("#password").fill("#03102005Ivelin");

  await page.getByRole("button", { name: "Sign In" }).click();

  await page.waitForURL("**/app/**");
  await page.waitForLoadState("networkidle");

  await expect(page.getByText("Login successful")).toBeVisible();
});
