import { test } from "@playwright/test";

test("test", async ({ page }) => {
  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign Up" }).click();
  await page.locator("#name").click();
  await page.locator("#name").fill("Ivelin Georgiev Vasilev");
  await page.locator("#email").click();
  await page.locator("#email").fill("ivelinvasilev4040@gmail.com");
  await page.locator("#password").click();
  await page.locator("#password").fill("#03102005Ivelin");
  await page.locator("#confirmPassword").click();
  await page.locator("#confirmPassword").fill("#03102005Ivelin");
  await page.getByRole("textbox", { name: "Local phone number" }).click();
  await page
    .getByRole("textbox", { name: "Local phone number" })
    .fill("630477211");
  await page.locator("#roles").selectOption("OWNER");
  await page.getByRole("button", { name: "Sign Up" }).click();
});
