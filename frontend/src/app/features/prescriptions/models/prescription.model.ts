import { DosageUnit } from './dosage-unit.model';

export interface PrescriptionRequest {
  patientId: number;
  medicationId: number;
  dosageAmount: number;
  dosageUnit: DosageUnit;
  frequency: string;
  scheduledTime: string | null;
  startDate: string;
  continuousUse: boolean;
  stockQuantity: number | null;
  minimumStockAlert: number | null;
  instructions: string | null;
}

export interface Prescription {
  id: number;
  patientId: number;
  patientName: string;
  medicationId: number;
  medicationName: string;
  dosageAmount: number;
  dosageUnit: DosageUnit;
  dosageUnitDisplayName: string;
  frequency: string;
  scheduledTime: string | null;
  startDate: string;
  continuousUse: boolean;
  stockQuantity: number | null;
  minimumStockAlert: number | null;
  instructions: string | null;
  active: boolean;
}
