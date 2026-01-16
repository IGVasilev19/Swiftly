import { test, expect } from "@playwright/test";

test("test", async ({ page }) => {
  await page.goto("http://localhost:5173/app/vehicles");
  await page.waitForLoadState("networkidle");
  
  const table = page.locator("table").first();
  await table.waitFor({ state: "visible", timeout: 10000 });
  
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
    selectedRow = rows.first();
  }
  
  const firstCell = selectedRow.locator("td").first();
  await firstCell.waitFor({ state: "visible", timeout: 10000 });
  await firstCell.click();
  await page.waitForLoadState("networkidle");
  
  const listButton = page.getByRole("button", { name: "List Vehicle +" });
  const editListingButton = page.getByRole("button", { name: "Edit Listing" });
  
  if (await editListingButton.isVisible({ timeout: 2000 }).catch(() => false)) {
    await editListingButton.click();
    await page.waitForLoadState("networkidle");
    const deleteButton = page.getByRole("button", { name: "Delete" });
    await deleteButton.waitFor({ state: "visible", timeout: 10000 });
    await deleteButton.click();
    await page.waitForLoadState("networkidle");
    await page.waitForTimeout(1000);
    await listButton.waitFor({ state: "visible", timeout: 10000 });
  } else {
    await listButton.waitFor({ state: "visible", timeout: 10000 });
  }
  
  await listButton.click();
  await page.getByRole("textbox", { name: "Title" }).click();
  await page
    .getByRole("textbox", { name: "Title" })
    .fill("Honda hybrid for rent");
  await page.getByRole("spinbutton", { name: "Price per day - €" }).click();
  await page
    .getByRole("spinbutton", { name: "Price per day - €" })
    .press("Shift+ArrowLeft");
  await page
    .getByRole("spinbutton", { name: "Price per day - €" })
    .press("Shift+ArrowDown");
  await page
    .getByRole("spinbutton", { name: "Price per day - €" })
    .fill("-0.1");
  await page
    .getByRole("spinbutton", { name: "Price per day - €" })
    .press("Shift+ArrowLeft");
  await page
    .getByRole("spinbutton", { name: "Price per day - €" })
    .press("Shift+ArrowLeft");
  await page
    .getByRole("spinbutton", { name: "Price per day - €" })
    .press("Shift+ArrowLeft");
  await page
    .getByRole("spinbutton", { name: "Price per day - €" })
    .press("Shift+ArrowLeft");
  await page
    .getByRole("spinbutton", { name: "Price per day - €" })
    .press("Shift+ArrowLeft");
  await page.getByRole("spinbutton", { name: "Price per day - €" }).fill("50");
  await page.getByRole("textbox", { name: "Enter Description" }).click();
  await page
    .getByRole("textbox", { name: "Enter Description" })
    .fill("Renting out a Honda hybrid in the Eindhoven region");
  await page.getByRole("button", { name: "Create Listing" }).click();
});
