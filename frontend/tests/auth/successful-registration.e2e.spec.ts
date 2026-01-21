import { test, expect } from "@playwright/test";

test("successful registration shows a success message", async ({ page }) => {
  const timestamp = Date.now();
  const email = `e2e+${timestamp}@example.com`;

  await page.goto("http://localhost:5173/auth/register");

  const responsePromise = page.waitForResponse(
    (response) =>
      response.url().includes("/auth/register") &&
      response.request().method() === "POST"
  );

  await page.locator("#name").fill("E2E Test User");
  await page.locator("#email").fill(email);
  await page.locator("#password").fill("Password123!");
  await page.locator("#confirmPassword").fill("Password123!");
  await page.getByLabel("Phone").fill("+31612345678");
  await page.locator("#roles").selectOption("RENTER");

  await page.getByRole("button", { name: "Sign Up" }).click();

  const response = await responsePromise;
  const data = (await response.json()) as { message?: string };

  await page.waitForURL("**/");
  await page.waitForLoadState("networkidle");

  expect(response.ok()).toBeTruthy();

  if (data.message) {
    await expect(page.getByText(data.message)).toBeVisible();
  }
});
