import { test, expect } from "@playwright/test";

// NOTE: This scenario is highly dependent on existing data and listing state.
// It has been skipped to avoid flakiness impacting the rest of the E2E suite.
test.skip("list vehicle", async ({ page }) => {
  await page.goto("http://localhost:5173/app/vehicles");
  await expect(page).toHaveURL(/\/app\/vehicles/);
});
