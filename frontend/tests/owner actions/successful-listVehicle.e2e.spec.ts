import { test, expect } from "@playwright/test";

test.skip("list vehicle", async ({ page }) => {
  await page.goto("http://localhost:5173/app/vehicles");
  await expect(page).toHaveURL(/\/app\/vehicles/);
});
