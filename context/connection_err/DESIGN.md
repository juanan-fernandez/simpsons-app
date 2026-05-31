---
name: Springfield Modern
colors:
  surface: '#fbfaee'
  surface-dim: '#dbdbcf'
  surface-bright: '#fbfaee'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f5f4e8'
  surface-container: '#efeee3'
  surface-container-high: '#e9e9dd'
  surface-container-highest: '#e4e3d7'
  on-surface: '#1b1c15'
  on-surface-variant: '#4c4732'
  inverse-surface: '#303129'
  inverse-on-surface: '#f2f1e5'
  outline: '#7e775f'
  outline-variant: '#cfc6ab'
  surface-tint: '#6f5d00'
  primary: '#6f5d00'
  on-primary: '#ffffff'
  primary-container: '#fed90f'
  on-primary-container: '#705f00'
  inverse-primary: '#e7c500'
  secondary: '#00658f'
  on-secondary: '#ffffff'
  secondary-container: '#48befe'
  on-secondary-container: '#004a6b'
  tertiary: '#006e1c'
  on-tertiary: '#ffffff'
  tertiary-container: '#8df28a'
  on-tertiary-container: '#00701d'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#ffe263'
  primary-fixed-dim: '#e7c500'
  on-primary-fixed: '#221b00'
  on-primary-fixed-variant: '#534600'
  secondary-fixed: '#c7e7ff'
  secondary-fixed-dim: '#86cfff'
  on-secondary-fixed: '#001e2e'
  on-secondary-fixed-variant: '#004c6d'
  tertiary-fixed: '#94f990'
  tertiary-fixed-dim: '#78dc77'
  on-tertiary-fixed: '#002204'
  on-tertiary-fixed-variant: '#005313'
  background: '#fbfaee'
  on-background: '#1b1c15'
  surface-variant: '#e4e3d7'
  status-alive: '#4CAF50'
  status-deceased: '#616161'
  card-bg: '#FFFBEB'
  ink-black: '#1A1A1A'
typography:
  headline-xl:
    fontFamily: Bricolage Grotesque
    fontSize: 48px
    fontWeight: '800'
    lineHeight: 56px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Bricolage Grotesque
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
  headline-md:
    fontFamily: Bricolage Grotesque
    fontSize: 24px
    fontWeight: '700'
    lineHeight: 32px
  body-lg:
    fontFamily: Hanken Grotesk
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: Hanken Grotesk
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  label-bold:
    fontFamily: Hanken Grotesk
    fontSize: 14px
    fontWeight: '700'
    lineHeight: 20px
  label-sm:
    fontFamily: Hanken Grotesk
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 8px
  gutter: 24px
  margin: 32px
  container-max-width: 1280px
---

## Brand & Style

The design system is a nostalgic yet polished tribute to the iconic Springfield aesthetic. It balances the high-energy, vibrant personality of a classic animated sitcom with the clean structure of modern SaaS design. The target audience includes fans, developers, and collectors who value a playful, accessible interface that doesn't sacrifice professional usability.

The design style is **Modern / High-Contrast**, characterized by bold blocks of color, thick-yet-soft shadows, and generous spacing. It avoids the chaotic "brutalism" often associated with retro themes in favor of a "polished cartoon" look—utilizing smooth transitions, rounded corners, and a curated palette to evoke warmth, familiarity, and optimism.

## Colors

The palette is driven by "Springfield Yellow" as the primary energetic driver, used for calls to action and key accents. "Springfield Blue" serves as the foundational structural color for headers and navigation, providing a calm, sky-like contrast to the vibrant yellow.

- **Primary (#FED90F):** Reserved for interactive elements, highlights, and primary brand touchpoints.
- **Secondary (#009DDC):** Used for top-level navigation backgrounds, section headers, and link text.
- **Neutral (#FDFCF0):** A warm, off-white cream used for the global background to reduce eye strain compared to pure white.
- **Surface Tiering:** Cards and containers use a slightly lighter cream (#FFFBEB) to subtly lift them from the background.
- **Status Indicators:** Explicitly uses "Alive" Green and "Deceased" Charcoal for character data, ensuring high visibility and semantic clarity.

## Typography

This design system uses a dual-font approach to marry personality with performance:

1.  **Display (Bricolage Grotesque):** A quirky, variable sans-serif with expressive terminals. It captures the "fun and rounded" requirement perfectly, giving headers a hand-drawn, animated quality. Use this for all H1–H3 levels.
2.  **Interface (Hanken Grotesk):** A clean, modern grotesque that provides high legibility for character bios, data points, and metadata. It keeps the UI feeling "SaaS-grade" and professional.

**Responsive Rules:** Headlines scale down by 20% on mobile devices, while body text remains consistent to ensure readability.

## Layout & Spacing

The layout utilizes a **12-column fluid grid** for desktop and a **single-column stack** for mobile. 

- **Spacing Rhythm:** Based on an 8px scale.
- **Margins & Gutters:** Generous 24px gutters ensure that the bold, rounded cards have plenty of "breathing room," preventing the interface from feeling cluttered despite the high-saturation colors.
- **Padding:** Internal card padding should be a minimum of 24px (3 units) to maintain the "soft and approachable" feel.
- **Alignment:** Content is generally center-aligned for marketing sections and left-aligned for data-heavy character views.

## Elevation & Depth

Depth is achieved through **Soft Tonal Layering** and **Ambient Shadows** rather than harsh outlines.

- **The "Springfield Lift":** Surfaces use a primary shadow that is extra-diffused (Blur: 20px) with a low-opacity tint of the secondary blue (e.g., `rgba(0, 157, 220, 0.08)`). This keeps the shadows looking "clean" and intentional.
- **Interactive States:** Buttons and cards should appear to "sink" slightly on hover or click, achieved by reducing the shadow spread and slightly darkening the surface color.
- **Layer Hierarchy:** 
    - Level 0: Global Background (Neutral Cream).
    - Level 1: Cards and Content Blocks (Light Cream).
    - Level 2: Modals and Popovers (Pure White with 15% opacity blue shadow).

## Shapes

The shape language is dominated by high-radius curves to reflect the friendly, animated source material. 

- **Base Radius:** 8px (0.5rem) for small components like inputs and chips.
- **Large Radius:** 16px (1rem) for character cards and main content containers.
- **Pill Radius:** Used exclusively for status badges (Alive/Deceased) and primary action buttons to make them feel "squishy" and inviting.
- **Strict Rule:** Avoid sharp 0px corners entirely to maintain the soft, polished aesthetic.

## Components

### Buttons
- **Primary:** Springfield Yellow (#FED90F) background with black text. 16px roundedness. Bold Bricolage Grotesque labels.
- **Secondary:** Springfield Blue (#009DDC) background with white text. 
- **Hover State:** A 10% brightness increase and a slight "pop-up" shadow transition.

### Cards (Character Profile)
- **Background:** Off-white cream (#FFFBEB).
- **Header:** A top-border or header-strip in Springfield Blue.
- **Border:** A very thin (1px) subtle border in a darker cream to define edges on high-brightness screens.
- **Corner Radius:** 16px.

### Status Chips
- **Alive:** Green (#4CAF50) background, white text, pill-shaped.
- **Deceased:** Charcoal (#616161) background, white text, pill-shaped.
- Include a small circular dot icon inside the chip for visual accessibility.

### Input Fields
- Soft, rounded (8px) borders.
- Background should be pure white to contrast against the cream page background.
- Focus state: 2px Springfield Blue border with a soft blue outer glow.

### Lists & Data Rows
- Alternate row backgrounds are not used; instead, use subtle horizontal dividers in a light gray-blue or use individual cards for each list item to maximize the "tactile" feel.