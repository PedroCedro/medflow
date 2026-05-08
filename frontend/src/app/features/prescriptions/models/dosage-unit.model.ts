export type DosageUnit = 'TABLET' | 'CAPSULE' | 'DROP' | 'ML' | 'MG' | 'G' | 'PUFF' | 'SPOON';

export const DOSAGE_UNIT_OPTIONS: Array<{ value: DosageUnit; label: string }> = [
  { value: 'TABLET', label: 'Comprimido' },
  { value: 'CAPSULE', label: 'Cápsula' },
  { value: 'DROP', label: 'Gota' },
  { value: 'ML', label: 'mL' },
  { value: 'MG', label: 'mg' },
  { value: 'G', label: 'g' },
  { value: 'PUFF', label: 'Jato' },
  { value: 'SPOON', label: 'Colher' }
];
