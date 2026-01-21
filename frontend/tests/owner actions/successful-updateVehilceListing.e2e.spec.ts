import { test, expect } from "@playwright/test";

test("update vehicle listing", async ({ page }) => {
  // Owner is already authenticated via storageState in Playwright config
  const responsePromise = page.waitForResponse(
    (response) =>
      response.url().includes("/listing/") &&
      response.request().method() === "PUT"
  );

  await page.goto("http://localhost:5173/app/vehicles");
  await page.waitForLoadState("networkidle");
  
  const table = page.locator("table").first();
  await table.waitFor({ state: "visible", timeout: 15000 });
  
  const rows = table.locator("tbody tr");
  const rowCount = await rows.count();
  
  if (rowCount === 0) {
    throw new Error("No vehicles found in the table");
  }
  
  let foundListed = false;
  let selectedRow = null;
  
  for (let i = 0; i < rowCount; i++) {
    const row = rows.nth(i);
    const cells = row.locator("td");
    const listedCell = cells.last();
    const listedText = await listedCell.textContent();
    
    if (listedText?.trim() === "Yes") {
      selectedRow = row;
      foundListed = true;
      break;
    }
  }
  
  if (!foundListed) {
    throw new Error("No listed vehicles found in the table. Cannot test update listing.");
  }
  
  const firstCell = selectedRow.locator("td").first();
  await firstCell.waitFor({ state: "visible", timeout: 15000 });
  await firstCell.click();
  await page.waitForLoadState("networkidle");
  await page.waitForTimeout(1000);
  
  const editListingButton = page.getByRole("button", { name: "Edit Listing" });
  await editListingButton.waitFor({ state: "visible", timeout: 15000 });
  await editListingButton.click();
  await page.waitForLoadState("networkidle");
  await page.waitForTimeout(1000);
  
  const titleInput = page.getByRole("textbox", { name: "Title" });
  await titleInput.waitFor({ state: "visible", timeout: 15000 });
  await titleInput.click();
  await titleInput.clear();
  await titleInput.fill("Honda hybrid for rent right now");
  
  const priceInput = page.getByRole("spinbutton", { name: "Price per day - €" });
  await priceInput.waitFor({ state: "visible", timeout: 15000 });
  await priceInput.fill("65");
  
  const descriptionInput = page.getByRole("textbox", { name: "Enter Description" });
  await descriptionInput.waitFor({ state: "visible", timeout: 15000 });
  await descriptionInput.click();
  await descriptionInput.clear();
  await descriptionInput.fill("Renting out a white Honda hybrid in the Eindhoven region.");
  
  const updateButton = page.getByRole("button", { name: "Update Listing" });
  await updateButton.waitFor({ state: "visible", timeout: 15000 });
  await updateButton.click();
  await page.waitForURL(/.*\/app\/vehicles/, { timeout: 15000 });
  await page.waitForLoadState("networkidle");

  const response = await responsePromise;
  const data = (await response.json()) as { message?: string };

  expect(response.ok()).toBeTruthy();
  if (data.message) {
    await expect(page.getByText(data.message)).toBeVisible();
  }
});
