import { defineConfig, devices } from "@playwright/test";

export default defineConfig({
  testDir: "./tests",
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  workers: process.env.CI ? 1 : undefined,
  reporter: "html",
  use: {
    trace: "on-first-retry",
  },

  projects: [
    {
      name: "setup",
      testMatch: /.*\.setup\.ts/,
    },
    {
      name: "renter",
      use: {
        storageState: "playwright/.auth/renter.json",
      },
      dependencies: ["setup"],
      testMatch: /.*\.e2e\.spec\.ts/,
      testIgnore: /.*owner actions.*/,
    },
    {
      name: "owner",
      use: {
        storageState: "playwright/.auth/owner.json",
      },
      dependencies: ["setup"],
      testMatch: /.*\.e2e\.spec\.ts/,
    },
    {
      name: "chromium",
      use: { ...devices["Desktop Chrome"] },
      testIgnore: /.*owner actions.*|.*auth.*/,
    },

    {
      name: "firefox",
      use: { ...devices["Desktop Firefox"] },
      testIgnore: /.*owner actions.*|.*auth.*/,
    },

    {
      name: "webkit",
      use: { ...devices["Desktop Safari"] },
      testIgnore: /.*owner actions.*|.*auth.*/,
    },
  ],

  webServer: {
    command: 'pnpm run dev',
    url: 'http://localhost:5173',
    reuseExistingServer: !process.env.CI,
    timeout: 120 * 1000,
  },
});
