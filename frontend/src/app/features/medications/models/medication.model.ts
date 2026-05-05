export interface MedicationRequest {
  name: string;
  activeIngredient: string | null;
  presentation: string | null;
  notes: string | null;
}

export interface Medication {
  id: number;
  name: string;
  activeIngredient: string | null;
  presentation: string | null;
  notes: string | null;
  active: boolean;
}
