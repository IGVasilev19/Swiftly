import { test, expect } from "@playwright/test";

test.skip("add vehicle", async ({ page }) => {
  await page.goto("http://localhost:5173/app/vehicles");
  await expect(page).toHaveURL(/\/app\/vehicles/);
});
