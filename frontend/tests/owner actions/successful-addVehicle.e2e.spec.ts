import { test, expect } from "@playwright/test";

test("add vehicle", async ({ page }) => {
  await page.goto("http://localhost:5173/app/vehicles");
  await page.waitForLoadState("networkidle");
  await page.getByRole("button", { name: "Add +" }).click();
  await page.waitForLoadState("networkidle");
  await page.getByRole("textbox", { name: "VIN" }).click();
  await page.getByRole("textbox", { name: "VIN" }).fill("86472301947328215");
  await page.getByRole("textbox", { name: "Make" }).click();
  await page.getByRole("textbox", { name: "Make" }).fill("Honda");
  await page.getByRole("textbox", { name: "Model" }).click();
  await page.getByRole("textbox", { name: "Model" }).fill("CRV");
  await page.getByRole("textbox", { name: "Color" }).click();
  await page.getByRole("textbox", { name: "Color" }).fill("White");
  await page.getByRole("spinbutton", { name: "Year" }).click();
  await page.getByRole("spinbutton", { name: "Year" }).fill("2023");
  await page.getByLabel("Vehicle Type").selectOption("SUV");
  await page.getByLabel("Fuel Type").selectOption("HYBRID");
  await page
    .getByRole("spinbutton", { name: "Fuel Consumption (L/100km)" })
    .click();
  await page
    .getByRole("spinbutton", { name: "Fuel Consumption (L/100km)" })
    .fill("5.5");
  await page.getByRole("button", { name: "NL Netherlands" }).click();
  await page.getByRole("textbox", { name: "City" }).click();
  await page.getByRole("textbox", { name: "City" }).fill("Eindhoven");
  await page.getByRole("checkbox", { name: "Android Auto" }).check();
  await page.getByRole("checkbox", { name: "Air Conditioning" }).check();
  await page.getByRole("checkbox", { name: "Apple Car Play" }).check();
  await page.locator("label").filter({ hasText: "Navigation" }).click();
  await page.locator(".relative.group-hover\\/file\\:shadow-2xl").click();
  const fileInput = page.locator('input[type="file"]#file-upload-handle');
  await fileInput.waitFor({ state: "attached", timeout: 5000 });
  await fileInput.setInputFiles([
    "tests/fixtures/586935966_122193135962502927_4684201845725767914_n.jpg",
    "tests/fixtures/588700105_122193135842502927_725002617815935457_n.jpg",
    "tests/fixtures/588726094_122193135872502927_3282858781627737672_n.jpg",
  ]);
  await page.getByRole("button", { name: "Add Vehicle" }).click();
});
