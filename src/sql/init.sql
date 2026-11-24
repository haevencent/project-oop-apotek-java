DELETE FROM apotek.detail_transaksi;
DELETE FROM apotek.transaksi; 
DELETE FROM apotek.batch_obat;
DELETE FROM apotek.obat;
DELETE FROM apotek.pengguna;

INSERT INTO apotek.pengguna(id_user, username, password)
    VALUES('USR-001', 'celine', 'pass123'),
        ('USR-002', 'cranley', 'pass456'),
        ('USR-003', 'felicia', 'pass789'),
        ('USR-004', 'roy', 'pass101'),
        ('USR-005', 'clarissa', 'pass202'),
        ('USR-006', 'dimasd', 'pass303'),
        ('USR-007', 'fajar', 'pass404'),
        ('USR-008', 'gita', 'pass505'),
        ('USR-009', 'hana', 'pass606'),
        ('USR-010', 'ivan', 'pass707'),
        ('USR-011', 'jasmine', 'pass808'),
        ('USR-012', 'kevin', 'pass909'),
        ('USR-013', 'lina', 'pass112'),
        ('USR-014', 'mario', 'pass213'),
        ('USR-015', 'nina', 'pass314')

INSERT INTO apotek.obat(id_obat, nama_obat, harga, butuh_resep) 
    VALUES('OBT-001', 'Paracetamol 500mg', 5000.00, false),
        ('OBT-002', 'Amoxicillin 500mg', 15000.00, true),
        ('OBT-003', 'VITAMIN C 1000', 3000.00, false),
        ('OBT-004', 'Ibuprofen 400mg', 8500.00, false),
        ('OBT-005', 'Cefadroxil 500mg', 22000.00, true),
        ('OBT-006', 'Cetirizine 10mg', 7500.00, false),
        ('OBT-007', 'Omeprazole 20mg', 12000.00, true),
        ('OBT-008', 'Antasida Doen', 4500.00, false),
        ('OBT-009', 'Amlodipine 5mg', 18000.00, true),
        ('OBT-010', 'Metformin 500mg', 10000.00, true),
        ('OBT-011', 'Loratadine 10mg', 9000.00, false),
        ('OBT-012', 'Asam Mefenamat 500mg', 9500.00, true),
        ('OBT-013', 'Ambroxol 30mg', 6500.00, false),
        ('OBT-014', 'Salbutamol 2mg', 11000.00, true),
        ('OBT-015', 'Zinc Sulfate 20mg', 7000.00, false);

INSERT INTO apotek.batch_obat(id_batch, id_obat, stok, tgl_expired) 
    VALUES('BCH-101', 'OBT-001', 100, '2027-12-31'),
        ('BCH-102', 'OBT-002', 50, '2026-11-30'),
        ('BCH-103', 'OBT-003', 200, '2028-05-15'),
        ('BCH-104', 'OBT-004', 75, '2027-08-20'),
        ('BCH-105', 'OBT-001', 150, '2028-01-10'),
        ('BCH-106', 'OBT-005', 30, '2026-12-15'),
        ('BCH-107', 'OBT-006', 120, '2027-09-30'),
        ('BCH-108', 'OBT-002', 80, '2027-04-25'),
        ('BCH-109', 'OBT-007', 250, '2028-11-12'),
        ('BCH-110', 'OBT-008', 45, '2027-03-18'),
        ('BCH-111', 'OBT-009', 90, '2027-10-05'),
        ('BCH-112', 'OBT-010', 60, '2026-12-31'),
        ('BCH-113', 'OBT-003', 180, '2028-07-22'),
        ('BCH-114', 'OBT-005', 110, '2027-06-14'),
        ('BCH-115', 'OBT-011', 300, '2029-02-28');
        
INSERT INTO apotek.transaksi (id_transaksi, id_user, tgl_transaksi, total_harga)
    VALUES('TRX-001', 'USR-001', '2026-10-31 10:00:00', 10000.00),
        ('TRX-002', 'USR-002', '2026-10-31 11:30:00', 45000.00),
        ('TRX-003', 'USR-001', '2026-11-01 09:15:00', 15000.00),
        ('TRX-004', 'USR-015', '2026-11-02 14:20:00', 17000.00),
        ('TRX-005', 'USR-014', '2026-11-03 16:45:00', 20000.00),
        ('TRX-006', 'USR-004', '2026-11-04 08:30:00', 22000.00),
        ('TRX-007', 'USR-013', '2026-11-05 13:10:00', 22500.00),
        ('TRX-008', 'USR-005', '2026-11-06 10:50:00', 30000.00),
        ('TRX-009', 'USR-012', '2026-11-07 15:05:00', 24000.00),
        ('TRX-010', 'USR-002', '2026-11-08 11:25:00', 18000.00),
        ('TRX-011', 'USR-009', '2026-11-09 17:40:00', 18000.00),
        ('TRX-012', 'USR-001', '2026-11-10 09:00:00', 30000.00),
        ('TRX-013', 'USR-008', '2026-11-11 12:35:00', 18000.00),
        ('TRX-014', 'USR-003', '2026-11-12 16:15:00', 44000.00),
        ('TRX-015', 'USR-007', '2026-11-13 14:50:00', 45000.00)

INSERT INTO apotek.detail_transaksi (id_transaksi, id_batch, qty_beli, sub_total)
        VALUES('TRX-001', 'BCH-101', 2, 10000.00),
        ('TRX-002', 'BCH-102', 3, 45000.00),
        ('TRX-003', 'BCH-103', 5, 15000.00),
        ('TRX-004', 'BCH-104', 2, 17000.00),
        ('TRX-005', 'BCH-105', 4, 20000.00),
        ('TRX-006', 'BCH-106', 1, 22000.00),
        ('TRX-007', 'BCH-107', 3, 22500.00),
        ('TRX-008', 'BCH-108', 2, 30000.00),
        ('TRX-009', 'BCH-109', 2, 24000.00),
        ('TRX-010', 'BCH-110', 4, 18000.00),
        ('TRX-011', 'BCH-111', 1, 18000.00),
        ('TRX-012', 'BCH-112', 3, 30000.00),
        ('TRX-013', 'BCH-113', 6, 18000.00),
        ('TRX-014', 'BCH-114', 2, 44000.00),
        ('TRX-015', 'BCH-115', 5, 45000.00)









