import { IntakeStatus } from './intake-status.model';

export interface IntakeRequest {
  prescriptionId: number;
  scheduledAt: string;
  takenAt: string | null;
  status: IntakeStatus;
  notes: string | null;
}

export interface BulkIntakeUpdateItem {
  id: number;
  status: IntakeStatus;
  takenAt: string | null;
}

export interface Intake {
  id: number;
  prescriptionId: number;
  patientId: number;
  patientName: string;
  medicationId: number;
  medicationName: string;
  scheduledAt: string;
  takenAt: string | null;
  status: IntakeStatus;
  notes: string | null;
  active: boolean;
}
