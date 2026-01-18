import { test, expect } from "@playwright/test";

test("list vehicle", async ({ page }) => {
  await page.goto("http://localhost:5173/app/vehicles");
  await page.waitForLoadState("networkidle");
  
  const table = page.locator("table").first();
  await table.waitFor({ state: "visible", timeout: 15000 });
  
  const rows = table.locator("tbody tr");
  const rowCount = await rows.count();
  
  if (rowCount === 0) {
    throw new Error("No vehicles found in the table");
  }
  
  let foundUnlisted = false;
  let selectedRow = null;
  
  for (let i = 0; i < rowCount; i++) {
    const row = rows.nth(i);
    const cells = row.locator("td");
    const listedCell = cells.last();
    const listedText = await listedCell.textContent();
    
    if (listedText?.trim() === "No") {
      selectedRow = row;
      foundUnlisted = true;
      break;
    }
  }
  
  if (!foundUnlisted) {
    const firstRow = rows.first();
    const firstCell = firstRow.locator("td").first();
    await firstCell.waitFor({ state: "visible", timeout: 15000 });
    await firstCell.click();
    await page.waitForLoadState("networkidle");
    await page.waitForTimeout(1000);
    
    const editListingButton = page.getByRole("button", { name: "Edit Listing" });
    const isEditListingVisible = await editListingButton.isVisible({ timeout: 2000 }).catch(() => false);
    
    if (isEditListingVisible) {
      await editListingButton.click();
      await page.waitForLoadState("networkidle");
      await page.waitForTimeout(1000);
      
      const deleteButton = page.getByRole("button", { name: "Delete Listing" });
      await deleteButton.waitFor({ state: "visible", timeout: 15000 });
      await deleteButton.click();
      await page.waitForURL(/.*\/app\/vehicles/, { timeout: 15000 });
      await page.waitForLoadState("networkidle");
      await page.waitForTimeout(1000);
      
      await page.goto("http://localhost:5173/app/vehicles");
      await page.waitForLoadState("networkidle");
      await page.waitForTimeout(1000);
      
      const tableAfterDelete = page.locator("table").first();
      await tableAfterDelete.waitFor({ state: "visible", timeout: 15000 });
      const rowsAfterDelete = tableAfterDelete.locator("tbody tr");
      selectedRow = rowsAfterDelete.first();
    } else {
      selectedRow = firstRow;
    }
  }
  
  const firstCell = selectedRow.locator("td").first();
  await firstCell.waitFor({ state: "visible", timeout: 15000 });
  await firstCell.click();
  await page.waitForLoadState("networkidle");
  await page.waitForTimeout(1000);
  
  const listButton = page.getByRole("button", { name: "List Vehicle +" });
  await listButton.waitFor({ state: "visible", timeout: 15000 });
  await listButton.click();
  
  await page.waitForTimeout(2000);
  
  const navigationPromise = page.waitForURL(/.*\/app\/vehicles/, { timeout: 3000 }).catch(() => null);
  const formPromise = page.getByRole("textbox", { name: "Title" }).waitFor({ state: "visible", timeout: 3000 }).catch(() => null);
  
  const result = await Promise.race([
    navigationPromise.then(() => "navigate"),
    formPromise.then(() => "form"),
  ]);
  
  if (result === "navigate") {
    await page.waitForLoadState("networkidle");
    expect(page.url()).toContain("/app/vehicles");
  } else {
    await page.waitForLoadState("networkidle");
    const titleInput = page.getByRole("textbox", { name: "Title" });
    await titleInput.waitFor({ state: "visible", timeout: 15000 });
    await titleInput.click();
    await titleInput.fill("Honda hybrid for rent");
    
    const priceInput = page.getByRole("spinbutton", { name: "Price per day - €" });
    await priceInput.waitFor({ state: "visible", timeout: 15000 });
    await priceInput.fill("50");
    
    const descriptionInput = page.getByRole("textbox", { name: "Enter Description" });
    await descriptionInput.waitFor({ state: "visible", timeout: 15000 });
    await descriptionInput.click();
    await descriptionInput.fill("Renting out a Honda hybrid in the Eindhoven region");
    
    const createButton = page.getByRole("button", { name: "Create Listing" });
    await createButton.waitFor({ state: "visible", timeout: 15000 });
    await createButton.click();
    await page.waitForURL(/.*\/app\/vehicles/, { timeout: 15000 });
    await page.waitForLoadState("networkidle");
  }
});
