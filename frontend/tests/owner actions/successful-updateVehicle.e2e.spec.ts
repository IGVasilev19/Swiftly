import { test, expect } from "@playwright/test";

test("test", async ({ page }) => {
  await page.goto("http://localhost:5173/app/vehicles");
  await page.waitForLoadState("networkidle");
  const vehicleCell = page.getByRole("cell", { name: "Honda" }).first();
  await vehicleCell.waitFor({ state: "visible", timeout: 10000 });
  await vehicleCell.click();
  await page.waitForLoadState("networkidle");
  const editButton = page.getByRole("button", { name: "Edit Vehicle" });
  await editButton.waitFor({ state: "visible", timeout: 10000 });
  await editButton.click();
  await page.waitForLoadState("networkidle");
  await page
    .getByRole("spinbutton", { name: "Fuel Consumption (L/100km)" })
    .click();
  await page
    .getByRole("spinbutton", { name: "Fuel Consumption (L/100km)" })
    .click();
  await page
    .getByRole("spinbutton", { name: "Fuel Consumption (L/100km)" })
    .click();
  await page
    .getByRole("spinbutton", { name: "Fuel Consumption (L/100km)" })
    .fill("5");
  await page
    .getByRole("spinbutton", { name: "Fuel Consumption (L/100km)" })
    .click();
  await page
    .getByRole("spinbutton", { name: "Fuel Consumption (L/100km)" })
    .press("Shift+ArrowLeft");
  await page
    .getByRole("spinbutton", { name: "Fuel Consumption (L/100km)" })
    .press("Shift+ArrowLeft");
  await page
    .getByRole("spinbutton", { name: "Fuel Consumption (L/100km)" })
    .press("Shift+ArrowLeft");
  await page
    .getByRole("spinbutton", { name: "Fuel Consumption (L/100km)" })
    .fill("6");
  await page.locator("div").nth(2).click();
  await page.locator("label").filter({ hasText: "Lane Assist" }).click();
  await page.getByRole("checkbox", { name: "Distance Assist" }).check();
  await page.getByRole("checkbox", { name: "All Wheel Drive" }).check();
  await page.getByRole("button", { name: "Update Vehicle" }).click();
});
