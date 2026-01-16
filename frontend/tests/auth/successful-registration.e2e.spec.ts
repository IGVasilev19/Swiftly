import { test, expect } from "@playwright/test";

test("test", async ({ page }) => {
  await page.goto("http://localhost:5173/app");
  await expect(page).toHaveURL(/.*\/app\/.*/);
});
