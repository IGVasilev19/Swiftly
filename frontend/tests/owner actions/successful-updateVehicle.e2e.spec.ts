import { test, expect } from "@playwright/test";

test("update vehicle", async ({ page }) => {
  await page.goto("http://localhost:5173/app/vehicles");
  await page.waitForLoadState("networkidle");
  
  const table = page.locator("table").first();
  await table.waitFor({ state: "visible", timeout: 15000 });
  
  const rows = table.locator("tbody tr");
  const rowCount = await rows.count();
  
  if (rowCount === 0) {
    throw new Error("No vehicles found in the table");
  }
  
  const firstRow = rows.first();
  const firstCell = firstRow.locator("td").first();
  await firstCell.waitFor({ state: "visible", timeout: 15000 });
  await firstCell.click();
  await page.waitForLoadState("networkidle");
  await page.waitForTimeout(1000);
  
  const editButton = page.getByRole("button", { name: "Edit Vehicle" });
  await editButton.waitFor({ state: "visible", timeout: 15000 });
  await editButton.click();
  await page.waitForLoadState("networkidle");
  await page.waitForTimeout(1000);
  
  const fuelConsumptionInput = page.getByRole("spinbutton", { name: "Fuel Consumption (L/100km)" });
  await fuelConsumptionInput.waitFor({ state: "visible", timeout: 15000 });
  await fuelConsumptionInput.click();
  await fuelConsumptionInput.fill("6");
  
  await page.locator("label").filter({ hasText: "Lane Assist" }).click();
  await page.getByRole("checkbox", { name: "Distance Assist" }).check();
  await page.getByRole("checkbox", { name: "All Wheel Drive" }).check();
  
  const updateButton = page.getByRole("button", { name: "Update Vehicle" });
  await updateButton.waitFor({ state: "visible", timeout: 15000 });
  await updateButton.click();
  
  await page.waitForURL(/.*\/app\/vehicles/, { timeout: 15000 });
  await page.waitForLoadState("networkidle");
});
